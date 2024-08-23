package com.conamobile.sendsmsromchi.ui.screens.permission

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.conamobile.sendsmsromchi.core.service.SmsService
import com.conamobile.sendsmsromchi.core.util.AdminReceiver
import com.conamobile.sendsmsromchi.core.util.toast
import com.conamobile.sendsmsromchi.ui.screens.load.LoadScreen

object PermissionScreen : Screen {
   private fun readResolve(): Any = PermissionScreen
   
   @Composable
   override fun Content() {
      val context = LocalContext.current
      val navigator = LocalNavigator.currentOrThrow
      var nextButtonAvailable by remember {
         mutableStateOf(false)
      }
      
      val componentName by remember {
         mutableStateOf(
            ComponentName(
               context,
               AdminReceiver::class.java
            )
         )
      }
      val permissionsToRequest = arrayOf(
         Manifest.permission.SEND_SMS,
         Manifest.permission.READ_SMS,
         Manifest.permission.RECEIVE_SMS,
         Manifest.permission.RECEIVE_MMS,
         Manifest.permission.WRITE_EXTERNAL_STORAGE,
         Manifest.permission.READ_EXTERNAL_STORAGE,
         Manifest.permission.POST_NOTIFICATIONS,
      )
      val multiplePermissionsState = rememberLauncherForActivityResult(
         contract = ActivityResultContracts.RequestMultiplePermissions()
      ) { permissions ->
         permissions.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value
            if (isGranted && permissionName == Manifest.permission.SEND_SMS) {
               nextButtonAvailable = true
//               context.startForegroundService(Intent(context, SmsService::class.java))
            }
         }
      }
      
      Column(
         modifier = Modifier.fillMaxSize(),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center
      ) {
         Text(text = "Hammasiga qo'rqmasdan ruxsat bering")
         Spacer(modifier = Modifier.height(20.dp))
         //autolaunch
         Button(onClick = {
            try {
               val intent = Intent()
               val manufacturer = Build.MANUFACTURER
               when {
                  "xiaomi".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                     )
                  }
                  
                  "oppo".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                     )
                  }
                  
                  "vivo".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                     )
                  }
                  
                  "letv".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity"
                     )
                  }
                  
                  "honor".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity"
                     )
                  }
                  
                  "asus".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus.mobilemanager.powersaver.PowerSaverSettings"
                     )
                  }
                  
                  "nokia".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.evenwell.powersaving.g3",
                        "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"
                     )
                  }
                  
                  "huawei".equals(manufacturer, ignoreCase = true) -> {
                     intent.component = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                     )
                  }
               }
               context.startActivity(intent)
               context.toast("Shu programmani tanlang!")
            } catch (e: Exception) {
                context.toast("AutoStartda noma'lum xatolik, hech narsa bo'lmaganday tashlab keting")
            }
         }) {
            Text(text = "AutoLaunch")
         }
         //admin
         Button(onClick = {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(
               DevicePolicyManager.EXTRA_DEVICE_ADMIN,
               componentName
            )
            intent.putExtra(
               DevicePolicyManager.EXTRA_ADD_EXPLANATION,
               "Hello :)"
            )
            context.startActivity(intent)
         }) {
            Text(text = "Admin")
         }
         //overlay
         Button(onClick = {
            val intent = Intent(
               Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
               Uri.parse("package:${context.packageName}")
            )
            context.startActivity(intent)
         }) {
            Text(text = "Overlay")
         }
         //sms
         Button(onClick = {
            multiplePermissionsState.launch(permissionsToRequest)
         }) {
            Text(text = "SMS")
         }
         Spacer(modifier = Modifier.height(50.dp))
         AnimatedVisibility(visible = nextButtonAvailable) {
            Button(onClick = {
               navigator.push(LoadScreen)
            }) {
               Text(text = "Keyingisi")
            }
         }
      }
   }
}