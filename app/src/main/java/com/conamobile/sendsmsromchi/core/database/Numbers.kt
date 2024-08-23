package com.conamobile.sendsmsromchi.core.database

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "numbers")
data class Numbers(
   @PrimaryKey(autoGenerate = true)
   val id: Long? = null,
   val number: String,
   val isSend: Boolean = false,
)
