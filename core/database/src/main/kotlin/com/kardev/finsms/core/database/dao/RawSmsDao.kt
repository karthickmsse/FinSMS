package com.kardev.finsms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kardev.finsms.core.database.entity.RawSmsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RawSmsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sms: RawSmsEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM raw_sms WHERE smsId = :smsId)")
    suspend fun exists(smsId: String): Boolean

    @Query("SELECT * FROM raw_sms WHERE parseStatus = 'UNRECOGNIZED' ORDER BY receivedAt DESC")
    fun getUnrecognized(): Flow<List<RawSmsEntity>>

    @Query("UPDATE raw_sms SET parseStatus = 'IGNORED' WHERE smsId = :smsId")
    suspend fun markIgnored(smsId: String)
}
