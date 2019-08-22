package com.jyj.notificationlistenerservice.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import com.jyj.notificationlistenerservice.receiver.AlarmReceiver

/**
author :shengsheng
date :2019/8/5
i despised my soul when i committed a wrong,and comforted myself that others also commit wrong.
 */
class MyTimerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intervalTime=30*1000
        var triggerAtTime =SystemClock.elapsedRealtime()+intervalTime
        var i = Intent(this, AlarmReceiver::class.java)
        var pi = PendingIntent.getBroadcast(this, 0, i, 0)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi)
        return super.onStartCommand(intent, flags, startId)
    }
}