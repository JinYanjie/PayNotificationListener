package com.jyj.notificationlistenerservice

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.jyj.notificationlistenerservice.bean.DataBean
import com.jyj.notificationlistenerservice.bean.NewsEvent
import com.jyj.notificationlistenerservice.service.NotificationService
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
author :shengsheng
date :2019/8/22
i despised my soul when i committed a wrong,and comforted myself that others also commit wrong.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val TAG = "AAA"
        private val listNews = ArrayList<DataBean>()
        private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var hostName = "http://proxy.zndata.store:8888/checkout/listener/"
    }

    private val adapter: ListAdapter by lazy { ListAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.WHITE
        }

        setContentView(R.layout.activity_main)
        //注册订阅者
        EventBus.getDefault().register(this)

        btnSure.setOnClickListener(this)
        btnDef.setOnClickListener(this)
        btnPermission.setOnClickListener(this)

        mRecycler.layoutManager = LinearLayoutManager(this)
        mRecycler.layoutManager = LinearLayoutManager(this)
        mRecycler.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSure -> {
                val url = edText.text.toString()
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(this@MainActivity, "url内容为空", Toast.LENGTH_LONG).show()
                } else if (!url.startsWith("http://")) {
                    Toast.makeText(this@MainActivity, "url错误", Toast.LENGTH_LONG).show()
                } else {
                    hostName = url
                }
            }

            R.id.btnDef -> {
                hostName = "http://proxy.zndata.store:8888/checkout/listener/"
                Toast.makeText(this@MainActivity, "以设置为默认url", Toast.LENGTH_LONG).show()
            }

            R.id.btnPermission -> {
                if (!isEnabled()) {
                    startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                } else {
                    Toast.makeText(applicationContext, "监控器开关已打开", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgReceived(event: NewsEvent) {//

        //将金额发送到服务器
        sendMsg(event.mMsg)

        val split = event.mMsg?.split("款".toRegex())
        if (split.isNullOrEmpty() || split.size < 2) return
        val moneys = split[1].split("元".toRegex())

        val dataBean = DataBean(format.format(Date().time), moneys[0], "")
        listNews.add(0, dataBean)//添加数据
        adapter.setNewData(listNews)
        adapter.notifyDataSetChanged()


    }

    private fun sendMsg(mMsg: String?) {
        val formBody = FormBody.Builder()
                .add("amount", mMsg!!)
                .build()
        val request = Request.Builder()
                .url(hostName)
                .post(formBody)
                .build()
        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                Log.e(TAG, "发生成功:   $mMsg")
                Toast.makeText(this@MainActivity, "发送服务器成功", Toast.LENGTH_LONG).show()
            }
        })
    }


    public override fun onResume() {
        super.onResume()
        toggleNotificationListenerService()
    }

    override fun onDestroy() {
        super.onDestroy()
        toggleNotificationListenerService()
    }

    //重写onKeyDown事件即可   Android按返回键退出程序但不销毁，程序后台保留
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun toggleNotificationListenerService() {
        //把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作：
        val pm = packageManager
        pm.setComponentEnabledSetting(
                ComponentName(this, NotificationService::class.java!!),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)

        pm.setComponentEnabledSetting(
                ComponentName(this, NotificationService::class.java!!),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }

    // 判断是否打开了通知监听权限
    private fun isEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

}