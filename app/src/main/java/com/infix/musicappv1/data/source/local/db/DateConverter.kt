package com.infix.musicappv1.data.source.local.db

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(mili: Long): Date {
        return Date(mili)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long {
        return date?.time ?: 0
    }
}