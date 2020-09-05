package com.example.trackerhabits.di

import android.content.Context
import com.example.data.db.HabitDao
import com.example.data.db.HabitDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule()
{
    @Provides
    fun provideHabitDao(context: Context): HabitDao
    {
        return HabitDatabase.getDatabase(context).habitDao()
    }
}