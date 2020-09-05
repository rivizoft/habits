package com.example.data.impl

import com.example.data.server.HabitServer
import com.example.domain.Habit
import com.example.domain.HabitUID
import com.example.domain.interfaces.HabitsRemoteRepository
import java.io.IOException

class HabitsRemoteRepositoryImpl(private val habitRetrofitService: HabitServer) :
    HabitsRemoteRepository
{
    override fun getHabits(): List<Habit>?
    {
        return try
        {
            val request = habitRetrofitService.api.getHabits()
            val response = request.execute()
            response.body()
        }
        catch (e: IOException)
        {
            null
        }
    }

    override fun addHabit(habit: Habit): HabitUID?
    {
        return try
        {
            val request = habitRetrofitService.api.addHabit(habit)
            val response = request.execute()
            response.body()
        }
        catch (e: IOException)
        {
            null
        }
    }

    override fun deleteHabit(habit: Habit): String?
    {
        return try {
            val uid = HabitUID(habit.uid!!)
            val request = habitRetrofitService.api.deleteHabit(uid)
            val response = request.execute()
            response.message()
        }
        catch (e: IOException)
        {
            null
        }
    }
}