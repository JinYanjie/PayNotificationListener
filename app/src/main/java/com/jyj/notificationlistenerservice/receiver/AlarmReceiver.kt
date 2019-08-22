package com.jyj.notificationlistenerservice.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jyj.notificationlistenerservice.MainActivity
import com.jyj.notificationlistenerservice.service.MyTimerService
import okhttp3.*
import java.io.IOException

/**
author :shengsheng
date :2019/8/15
i despised my soul when i committed a wrong,and comforted myself that others also commit wrong.
 */
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AAA"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Thread(Runnable {
            sendHeartPackage()
        }).start()
        context?.startService(Intent(context, MyTimerService::class.java))
    }

    private fun sendHeartPackage() {
        val request = Request.Builder()
                .url(MainActivity.hostName)
                .get()
                .build()
        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.e(TAG, "心跳包")
            }
        })
    }
}