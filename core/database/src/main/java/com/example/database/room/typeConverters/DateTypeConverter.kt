package com.example.database.room.typeConverters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun fromDate(value:Date) = value.time

    @TypeConverter
    fun toDate(value:Long) = Date(value)
}