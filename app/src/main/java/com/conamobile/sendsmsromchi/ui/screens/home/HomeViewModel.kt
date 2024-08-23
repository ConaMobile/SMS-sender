package com.conamobile.sendsmsromchi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.conamobile.sendsmsromchi.core.database.Numbers
import com.conamobile.sendsmsromchi.core.database.NumbersDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
   private val numbersDao: NumbersDao,
) : ViewModel() {
   init {
      getSendSmsCount()
   }
   
   private val _totalSendSmsCount = MutableStateFlow(0)
   val totalSendSmsCount = _totalSendSmsCount.asStateFlow()
   
   fun saveAllNumber(numbers: List<Numbers>) {
      viewModelScope.launch {
         numbersDao.addNumbersList(numbers)
      }
   }
   
   private fun getSendSmsCount() {
      viewModelScope.launch(Dispatchers.IO) {
         while (true) {
            _totalSendSmsCount.emit(numbersDao.countIsSendTrue())
            delay(2000)
         }
      }
   }
   
   fun getAllNumbersCount(count: (Int) -> Unit) {
      viewModelScope.launch {
         count(numbersDao.count())
      }
   }
   
   fun disableAllSelectStatus() {
      viewModelScope.launch {
         numbersDao.disableAllSelectStatus()
      }
   }
   
}