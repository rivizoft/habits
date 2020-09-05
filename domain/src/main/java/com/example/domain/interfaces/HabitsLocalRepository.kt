package com.example.domain.interfaces

import com.example.domain.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsLocalRepository
{
    val allHabits: Flow<List<Habit>>

    fun addHabit(habit: Habit)
    fun updateHabit(habit: Habit)
    fun deleteHabit(habit: Habit)
    fun getAllHabitsWithTitleLike(title: String): List<Habit>
    fun getAllHabits(): List<Habit>
}