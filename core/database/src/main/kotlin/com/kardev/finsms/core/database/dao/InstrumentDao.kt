package com.kardev.finsms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kardev.finsms.core.common.InstrumentType
import com.kardev.finsms.core.database.entity.InstrumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentDao {
    @Insert
    suspend fun insert(instrument: InstrumentEntity): Long

    @Query("SELECT * FROM instruments WHERE type = :type AND identifier = :identifier LIMIT 1")
    suspend fun find(type: InstrumentType, identifier: String): InstrumentEntity?

    @Query("SELECT * FROM instruments WHERE isActive = 1")
    fun getAll(): Flow<List<InstrumentEntity>>

    suspend fun findOrCreate(type: InstrumentType, identifier: String, bankName: String): Long {
        find(type, identifier)?.let { return it.instrumentId }
        return insert(
            InstrumentEntity(
                type = type,
                identifier = identifier,
                bankName = bankName,
                displayName = "$bankName ${type.name.replace('_', ' ')} •$identifier"
            )
        )
    }
}
