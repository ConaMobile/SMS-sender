package com.conamobile.sendsmsromchi.core.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.conamobile.sendsmsromchi.R
import com.conamobile.sendsmsromchi.core.MySharedPreferences
import com.conamobile.sendsmsromchi.core.database.NumbersDao
import com.conamobile.sendsmsromchi.core.util.logError
import com.conamobile.sendsmsromchi.core.util.sendSms
import com.conamobile.sendsmsromchi.core.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SmsService : Service() {
   private val numbersDao: NumbersDao by inject()
   private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
   private val scopeIO = CoroutineScope(Dispatchers.IO + SupervisorJob())
   private val notification =
      NotificationCompat.Builder(this, "Romchi").setContentTitle("Ilova ishlamoqda...")
         .setContentText("Biroz kuting...").setSmallIcon(R.drawable.ic_launcher_foreground)
         .setPriority(NotificationCompat.PRIORITY_MAX).setAutoCancel(false).setOngoing(true)
   private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
   
   
   override fun onCreate() {
      super.onCreate()
      createChannel()
      startForeground(1, notification.build())
      startSmsSender()
   }
   
   private fun startSmsSender() {
      scopeIO.launch {
         numbersDao.getAllIsSendFalseNumbers().take(1).collect { numbers ->
            numbers.forEach {
               logError("startSmsSender: ${it.number}")
               try {
                  sendSms(
                     this@SmsService,
                     it.number,
                     MySharedPreferences(this@SmsService).smsText()
                  )
               } catch (e: Exception) {
                  this@SmsService.toast("${it.number} ga jo'natib bo'lmadi!")
               }
               notification.setContentText("${it.number} ga sms yuborildi")
               notificationManager.notify(0, notification.build())
               numbersDao.updateSendStatus(it.id!!, true)
               delay(10000)
            }
         }
      }
   }
   
   private fun createChannel() {
      val channel =
         NotificationChannel("Romchi", "Romchi", NotificationManager.IMPORTANCE_HIGH)
      channel.setSound(null, null)
      notificationManager.createNotificationChannel(channel)
   }
   
   override fun onBind(intent: Intent?): IBinder? = null
}