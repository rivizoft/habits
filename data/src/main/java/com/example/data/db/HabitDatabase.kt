package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.db.converters.HabitTypeConverter
import com.example.data.mapping.DbHabit

@Database(entities = [DbHabit::class], version = 1, exportSchema = false)
@TypeConverters(HabitTypeConverter::class)
abstract class HabitDatabase : RoomDatabase()
{
    abstract fun habitDao(): HabitDao

    companion object
    {
        @Volatile
        private var INSTANCE: HabitDatabase? = null
        
        fun getDatabase(context: Context): HabitDatabase
        {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            val instance = Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habits_database"
            ).allowMainThreadQueries().build()

            INSTANCE = instance
            return instance
        }
    }
}