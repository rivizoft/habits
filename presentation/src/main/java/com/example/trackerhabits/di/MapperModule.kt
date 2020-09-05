package com.example.trackerhabits.di

import com.example.data.mapping.Mapper
import dagger.Module
import dagger.Provides

@Module
class MapperModule
{
    @Provides
    fun provideMapper(): Mapper
    {
        return Mapper()
    }
}