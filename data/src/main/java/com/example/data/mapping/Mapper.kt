package com.example.data.mapping

import com.example.domain.Habit

class Mapper
{
    fun mapDomainHabitToDb(habit: Habit): DbHabit =
        DbHabit(
            habit.id,
            habit.uid,
            habit.title,
            habit.describe,
            habit.color,
            habit.type,
            habit.priority,
            habit.frequency,
            habit.date,
            habit.count,
            habit.isDelete,
            habit.isUpdated)

    fun mapDbHabitToDomain(dbHabit: DbHabit): Habit =
        Habit(
            dbHabit.id,
            dbHabit.uid,
            dbHabit.title,
            dbHabit.describe,
            dbHabit.color,
            dbHabit.type,
            dbHabit.priority,
            dbHabit.frequency,
            dbHabit.date,
            dbHabit.count,
            dbHabit.isDelete,
            dbHabit.isUpdated)

    fun mapDbHabitsToDomain(dbHabits: List<DbHabit>): List<Habit>
    {
        val result = mutableListOf<Habit>()
        dbHabits.forEach { habit ->
            result.add(mapDbHabitToDomain(habit))
        }
        return result
    }
}