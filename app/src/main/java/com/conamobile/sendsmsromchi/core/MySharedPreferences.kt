package com.conamobile.sendsmsromchi.core

import android.content.Context

class MySharedPreferences(context: Context) {
   private val pref = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
   
   fun newUser(set: Boolean) {
      val editor = pref.edit()
      editor.putBoolean("newUser", set)
      editor.apply()
   }
   
   fun newUser(): Boolean {
      return pref.getBoolean("newUser", true)
   }
   
   fun smsText(smsText: String) = pref.edit().putString("smsText", smsText).apply()
   
   fun smsText() = pref.getString("smsText", "") ?: ""
   
}