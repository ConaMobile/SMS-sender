package com.conamobile.sendsmsromchi.core.util

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook

fun Context.toast(message: String) {
   Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun getNumbersFromExcelFile(context: Context, fileName: String): List<String> {
   val numbersList = mutableListOf<String>()
   context.assets.open(fileName).use { inputStream ->
      val workbook = XSSFWorkbook(inputStream)
      val numberOfSheets = workbook.numberOfSheets
      for (sheetIndex in 0 until numberOfSheets) {
         val sheet = workbook.getSheetAt(sheetIndex)
         for (row in sheet) {
            for (cell in row) {
               cell.cellType = CellType.STRING
               numbersList.add(cell.toString())
            }
         }
      }
      workbook.close()
   }
   return numbersList
}

fun sendSms(context: Context, phoneNumber: String, text: String) {
   val smsManager = context.getSystemService(SmsManager::class.java)
   val message = smsManager.divideMessage(text)
   
   smsManager.sendMultipartTextMessage(
      phoneNumber,
      "Send SMS",
      message,
      null,
      null
   )
}

fun logError(message: String) {
   Log.e("@@@", message)
}