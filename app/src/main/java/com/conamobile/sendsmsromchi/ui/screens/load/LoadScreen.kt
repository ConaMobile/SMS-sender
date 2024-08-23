package com.conamobile.sendsmsromchi.ui.screens.load

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.conamobile.sendsmsromchi.core.MySharedPreferences
import com.conamobile.sendsmsromchi.core.database.Numbers
import com.conamobile.sendsmsromchi.core.util.getNumbersFromExcelFile
import com.conamobile.sendsmsromchi.ui.screens.home.HomeScreen
import com.conamobile.sendsmsromchi.ui.screens.home.HomeViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

object LoadScreen : Screen {
   private fun readResolve(): Any = LoadScreen
   
   @Composable
   override fun Content() {
      val context = LocalContext.current
      val navigator = LocalNavigator.currentOrThrow
      val viewmodel = koinViewModel<HomeViewModel>()
      val numbersList = remember { mutableStateListOf<String>() }
      var successfullyLoaded by remember {
         mutableStateOf(false)
      }
      
      LaunchedEffect(key1 = Unit) {
         delay(3000)
         numbersList.addAll(getNumbersFromExcelFile(context, "test.xlsx"))
         delay(1000)
         successfullyLoaded = true
      }
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
         Text(text = if (successfullyLoaded) "Done" else "Telfon raqamlar yuklanmoqda, biroz kuting...")
         AnimatedVisibility(visible = successfullyLoaded) {
            Button(onClick = {
               viewmodel.saveAllNumber(numbersList.map { Numbers(number = it) })
               MySharedPreferences(context).newUser(false)
               navigator.push(HomeScreen)
            }) {
               Text(text = "Keyingisi")
            }
         }
      }
   }
}