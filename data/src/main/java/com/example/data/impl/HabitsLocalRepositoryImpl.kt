package com.example.data.impl

import com.example.data.db.HabitDao
import com.example.data.mapping.Mapper
import com.example.domain.Habit
import com.example.domain.interfaces.HabitsLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitsLocalRepositoryImpl(private val habitDao: HabitDao,
                                private val mapper: Mapper) : HabitsLocalRepository
{
    override val allHabits: Flow<List<Habit>>
        get() =
            habitDao.allLiveData().map { mapper.mapDbHabitsToDomain(it) }

    override fun addHabit(habit: Habit)
    {
        val mapHabit = mapper.mapDomainHabitToDb(habit)
        habitDao.addHabit(mapHabit)
    }

    override fun updateHabit(habit: Habit)
    {
        val mapHabit = mapper.mapDomainHabitToDb(habit)
        habitDao.updateHabit(mapHabit)
    }

    override fun deleteHabit(habit: Habit)
    {
        val mapHabit = mapper.mapDomainHabitToDb(habit)
        habitDao.deleteHabit(mapHabit)
    }

    override fun getAllHabitsWithTitleLike(title: String): List<Habit>
    {
        val resultList = mutableListOf<Habit>()
        habitDao.getAllHabitsWithTitleLike(title).forEach { habit ->
            resultList.add(mapper.mapDbHabitToDomain(habit))
        }
        return resultList
    }

    override fun getAllHabits(): List<Habit>
    {
        val resultList = mutableListOf<Habit>()
        habitDao.getAllData().forEach { habit ->
            resultList.add(mapper.mapDbHabitToDomain(habit))
        }
        return resultList
    }
}