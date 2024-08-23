package com.conamobile.sendsmsromchi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.conamobile.sendsmsromchi.core.MySharedPreferences
import com.conamobile.sendsmsromchi.ui.screens.home.HomeScreen
import com.conamobile.sendsmsromchi.ui.screens.permission.PermissionScreen
import com.conamobile.sendsmsromchi.ui.theme.SendSMSromchiTheme

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         SendSMSromchiTheme(darkTheme = false) {
            Surface(modifier = Modifier
               .fillMaxSize()
               .systemBarsPadding()) {
               Navigator(screen = if (MySharedPreferences(this).newUser()) PermissionScreen else HomeScreen)
            }
         }
      }
   }
}