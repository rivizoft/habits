package com.example.data.db.converters

import androidx.room.TypeConverter
import com.example.domain.HabitType

class HabitTypeConverter
{
    companion object
    {
        @TypeConverter
        @JvmStatic
        fun fromHabitType(type: HabitType): String
        {
            return type.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toHabitType(str: String): HabitType
        {
            return when (str) {
                HabitType.BAD.toString() -> HabitType.BAD
                else -> HabitType.GOOD
            }
        }
    }
}