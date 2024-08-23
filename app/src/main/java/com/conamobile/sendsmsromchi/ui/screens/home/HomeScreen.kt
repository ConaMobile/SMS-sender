package com.conamobile.sendsmsromchi.ui.screens.home

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.conamobile.sendsmsromchi.core.MySharedPreferences
import com.conamobile.sendsmsromchi.core.service.SmsService
import com.conamobile.sendsmsromchi.core.util.logError
import org.koin.androidx.compose.koinViewModel

object HomeScreen : Screen {
   private fun readResolve(): Any = HomeScreen
   
   @Composable
   override fun Content() {
      val context = LocalContext.current
      val navigator = LocalNavigator.currentOrThrow
      var smsText by remember { mutableStateOf("") }
      var smsStarted by remember { mutableStateOf(false) }
      val viewmodel = koinViewModel<HomeViewModel>()
      val totalSendSmsCount by viewmodel.totalSendSmsCount.collectAsState()
      var allNumbersCount by remember { mutableIntStateOf(0) }
      var allDone by remember { mutableStateOf(false) }
      
      LaunchedEffect(key1 = totalSendSmsCount) {
         if (allNumbersCount > 0){
            if (totalSendSmsCount == allNumbersCount){
               smsStarted = false
               context.stopService(Intent(context, SmsService::class.java))
               allDone = true
            }
         }
      }
      
      LaunchedEffect(key1 = Unit) {
         smsText = MySharedPreferences(context).smsText()
         viewmodel.getAllNumbersCount {
            logError("total count: $it")
            allNumbersCount = it
         }
      }
      
      Column(
         modifier = Modifier.fillMaxSize(),
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         BasicTextField(
            modifier = Modifier
               .background(Color.Yellow)
               .fillMaxWidth()
               .height(300.dp)
               .padding(10.dp),
            value = smsText,
            onValueChange = { smsText = it },
         )
         Spacer(modifier = Modifier.height(30.dp))
         Button(onClick = {
            if (smsStarted) {
               context.stopService(Intent(context, SmsService::class.java))
            } else {
               allDone = false
               MySharedPreferences(context).smsText(smsText)
//               context.startForegroundService(Intent(context, SmsService::class.java))
               context.startService(Intent(context, SmsService::class.java))
            }
            smsStarted = !smsStarted
         }) {
            Text(text = if (smsStarted) "To'xtatish" else "Jo'natishni boshlash")
         }
         Spacer(modifier = Modifier.height(30.dp))
         Button(onClick = {
            context.stopService(Intent(context, SmsService::class.java))
         }) {
            Text(text = "Remove Notification and stop all")
         }
         Spacer(modifier = Modifier.height(30.dp))
         Text(text = "Jo'natildi: $totalSendSmsCount / $allNumbersCount")
         Spacer(modifier = Modifier.height(30.dp))
         AnimatedVisibility(visible = allDone) {
            Column {
               Text(text = "Barchaga muvaffaqiyatli jo'natildi!")
               Button(onClick = {
                  viewmodel.disableAllSelectStatus()
               }) {
                  Text(text = "Boshqa yana nimadur jo'natmoqchi bo'lsangiz bosing!")
               }
            }
         }
      }
   }
}