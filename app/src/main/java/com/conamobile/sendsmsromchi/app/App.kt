package com.conamobile.sendsmsromchi.app

import android.app.Application
import com.conamobile.sendsmsromchi.core.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
   override fun onCreate() {
      super.onCreate()
      startKoin {
         androidContext(this@App)
         modules(appModule)
      }
   }
}