package com.kardev.finsms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kardev.finsms.core.common.InstrumentType

@Entity(tableName = "instruments")
data class InstrumentEntity(
    @PrimaryKey(autoGenerate = true) val instrumentId: Long = 0,
    val type: InstrumentType,
    val identifier: String,
    val bankName: String,
    val displayName: String,
    val isActive: Boolean = true
)
