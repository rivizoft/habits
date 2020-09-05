package com.example.trackerhabits.di

import com.example.data.server.HabitServer
import dagger.Module
import dagger.Provides

@Module
class RetrofitModule
{
    @Provides
    fun provideRetrofit(): HabitServer
    {
        return HabitServer()
    }
}