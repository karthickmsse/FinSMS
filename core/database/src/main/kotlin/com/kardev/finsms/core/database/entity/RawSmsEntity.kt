package com.kardev.finsms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kardev.finsms.core.common.ParseStatus

@Entity(tableName = "raw_sms")
data class RawSmsEntity(
    @PrimaryKey val smsId: String,
    val sender: String,
    val body: String,
    val receivedAt: Long,
    val parseStatus: ParseStatus,
    val matchedTemplateId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
