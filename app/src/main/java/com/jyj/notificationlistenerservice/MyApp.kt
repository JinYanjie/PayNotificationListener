package com.jyj.notificationlistenerservice

import android.app.Application
import android.content.Intent
import com.jyj.notificationlistenerservice.service.MyTimerService
import com.jyj.notificationlistenerservice.service.NotificationService

/**
author :shengsheng
date :2019/8/22
i despised my soul when i committed a wrong,and comforted myself that others also commit wrong.
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, NotificationService::class.java))
        startService(Intent(this, MyTimerService::class.java))
    }
}