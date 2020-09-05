package com.example.trackerhabits.di

import android.app.Activity
import android.content.Context
import com.example.trackerhabits.MainActivity
import com.example.trackerhabits.fragments.AddOrEditHabitFragment
import com.example.trackerhabits.fragments.HabitsListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, DatabaseModule::class,
    HabitModule::class, MapperModule::class, RetrofitModule::class])
interface AppComponent
{
    val context: Context

    //Fragments
    fun inject(addOrEditHabitFragment: AddOrEditHabitFragment)
    fun inject(habitsListFragment: HabitsListFragment)

    //Activity
    fun inject(activity: MainActivity)
}