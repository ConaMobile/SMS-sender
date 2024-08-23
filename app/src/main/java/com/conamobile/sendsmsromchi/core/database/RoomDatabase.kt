package com.conamobile.sendsmsromchi.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
   entities = [Numbers::class],
   version = 1,
   exportSchema = false
)

abstract class RoomDatabase : RoomDatabase() {
   abstract fun mirrorDao(): NumbersDao
}