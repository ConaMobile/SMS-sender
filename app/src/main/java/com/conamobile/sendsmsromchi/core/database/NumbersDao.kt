package com.conamobile.sendsmsromchi.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.conamobile.sendsmsromchi.core.database.Numbers
import kotlinx.coroutines.flow.Flow

@Dao
interface NumbersDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addNumber(data: Numbers)
   
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addNumbersList(data: List<Numbers>)
   
   @Delete(entity = Numbers::class)
   suspend fun deleteNumber(data: Numbers)
   
   @Query("DELETE FROM numbers WHERE id = :id")
   suspend fun deleteNumberById(id: Long)
   
   @Query("SELECT * FROM numbers ORDER BY id ASC")
   fun getAllNumbers(): Flow<List<Numbers>>
   
   @Query("SELECT * FROM numbers WHERE isSend = 0 ORDER BY id ASC")
   fun getAllIsSendFalseNumbers(): Flow<List<Numbers>>
   
   @Query("DELETE FROM numbers")
   suspend fun deleteAllNumbers()
   
   @Query("UPDATE numbers SET isSend = :isSend WHERE id = :id")
   suspend fun updateSendStatus(id: Long, isSend: Boolean)
   
   @Query("UPDATE numbers SET isSend = :isSelect")
   suspend fun disableAllSelectStatus(isSelect: Boolean = false)
   
   @Query("SELECT COUNT(id) FROM numbers")
   suspend fun count(): Int
   
   @Query("SELECT COUNT(id) FROM numbers WHERE isSend = 1")
   suspend fun countIsSendTrue(): Int
}