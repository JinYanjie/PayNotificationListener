package com.jyj.notificationlistenerservice.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.util.Log
import com.jyj.notificationlistenerservice.bean.NewsEvent
import org.greenrobot.eventbus.EventBus

/**
author :shengsheng
date :2019/8/22
i despised my soul when i committed a wrong,and comforted myself that others also commit wrong.
 */

class NotificationService : NotificationListenerService() {
    private val aliPay = "com.eg.android.AlipayGphone"
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        //包名
        var packageName = sbn?.packageName

        var extras = sbn?.notification?.extras
        var tittle = extras?.getString(Notification.EXTRA_TITLE)
        var content = extras?.getString(Notification.EXTRA_TEXT)
        Log.i("NotificationService", "Notification posted   Pkg=$packageName     tittle=$tittle  \n  content=$content")

        if (TextUtils.equals(aliPay, packageName)) {
            if (TextUtils.equals("支付宝通知", tittle)) {
                EventBus.getDefault().post(NewsEvent(content, "支付宝"))
            }
        }

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        //移除消息暂不做处理
    }
}