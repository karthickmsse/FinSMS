package com.kardev.finsms.core.database

import androidx.room.TypeConverter
import com.kardev.finsms.core.common.Direction
import com.kardev.finsms.core.common.InstrumentType
import com.kardev.finsms.core.common.ParseStatus

class Converters {
    @TypeConverter
    fun fromDirection(value: Direction): String = value.name
    @TypeConverter
    fun toDirection(value: String): Direction = Direction.valueOf(value)

    @TypeConverter
    fun fromInstrumentType(value: InstrumentType): String = value.name
    @TypeConverter
    fun toInstrumentType(value: String): InstrumentType = InstrumentType.valueOf(value)

    @TypeConverter
    fun fromParseStatus(value: ParseStatus): String = value.name
    @TypeConverter
    fun toParseStatus(value: String): ParseStatus = ParseStatus.valueOf(value)
}
