package com.conamobile.sendsmsromchi.core.koin

import android.content.Context
import androidx.room.Room
import com.conamobile.sendsmsromchi.core.database.RoomDatabase
import com.conamobile.sendsmsromchi.ui.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
   single { myDatabase(get()) }
   single { myDatabase(get()).mirrorDao() }
   viewModel { HomeViewModel(get()) }
}

fun myDatabase(context: Context): RoomDatabase {
   return Room.databaseBuilder(context, RoomDatabase::class.java, "database.db")
      .fallbackToDestructiveMigration().build()
}