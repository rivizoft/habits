package com.example.domain.interfaces

import com.example.domain.Habit
import com.example.domain.HabitUID

interface HabitsRemoteRepository
{
    fun getHabits(): List<Habit>?
    fun addHabit(habit: Habit): HabitUID?
    fun deleteHabit(habit: Habit): String?
}