package com.example.trackerhabits.di

import com.example.data.db.HabitDao
import com.example.data.server.HabitServer
import com.example.data.impl.HabitsLocalRepositoryImpl
import com.example.data.impl.HabitsRemoteRepositoryImpl
import com.example.data.mapping.Mapper
import com.example.domain.usecases.HabitIntercept
import com.example.domain.interfaces.HabitsLocalRepository
import com.example.domain.interfaces.HabitsRemoteRepository
import dagger.Module
import dagger.Provides

@Module
class HabitModule
{
    @Provides
    fun provideHabitIntercept(habitsLocalRepository: HabitsLocalRepository,
                              habitsRemoteRepository: HabitsRemoteRepository
    ): HabitIntercept
    {
        return HabitIntercept(habitsLocalRepository, habitsRemoteRepository)
    }

    @Provides
    fun provideHabitsLocalRepository(habitDao: HabitDao, mapper: Mapper): HabitsLocalRepository
    {
        return HabitsLocalRepositoryImpl(habitDao, mapper)
    }

    @Provides
    fun provideHabitsRemoteRepository(retrofitService: HabitServer): HabitsRemoteRepository
    {
        return HabitsRemoteRepositoryImpl(retrofitService)
    }
}