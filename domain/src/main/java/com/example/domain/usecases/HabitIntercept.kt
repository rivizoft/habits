package com.example.domain.usecases

import com.example.domain.Habit
import com.example.domain.HabitType
import com.example.domain.HabitUID
import com.example.domain.interfaces.HabitsLocalRepository
import com.example.domain.interfaces.HabitsRemoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import java.util.concurrent.TimeUnit

class HabitIntercept(private val habitsLocalRepository: HabitsLocalRepository,
                     private val habitsRemoteRepository: HabitsRemoteRepository
)
{
    fun getAllHabitsFromDb(): Flow<List<Habit>> = habitsLocalRepository.allHabits

    suspend fun addHabitToDb(habit: Habit)
    {
        habitsLocalRepository.addHabit(habit)
    }

    suspend fun addOrUpdateHabitToServer(habit: Habit): HabitUID?
    {
        return habitsRemoteRepository.addHabit(habit)
    }

    suspend fun deleteHabitFromDb(habit: Habit)
    {
        habitsLocalRepository.deleteHabit(habit)
    }

    suspend fun deleteHabitFromServer(habit: Habit): String?
    {
        return habitsRemoteRepository.deleteHabit(habit)
    }

    fun updateHabitInDb(habit: Habit)
    {
        habitsLocalRepository.updateHabit(habit)
    }

    fun getAllHabitsFromDbWithTitleLike(title: String): List<Habit>
    {
        return habitsLocalRepository.getAllHabitsWithTitleLike(title)
    }

    fun getListHabitsFromDb(): List<Habit>
    {
        return habitsLocalRepository.getAllHabits()
    }
    
    suspend fun getListHabitsFromServer(): List<Habit>?
    {
        return habitsRemoteRepository.getHabits()
    }

    fun doneHabitAndUpdateInBd(habit: Habit): String
    {
        if (habit.count != 0)
        {
            habit.count--
            habit.isUpdated = true
            habitsLocalRepository.updateHabit(habit)
        }

        val currentDate = Date().time
        val diffTime = currentDate - habit.date
        val expiredHabit = TimeUnit.MILLISECONDS.toDays(diffTime) > habit.frequency

        if (expiredHabit)
            return "Привычка уже истекла :("

        return when (habit.type) {
            HabitType.BAD -> {
                if (habit.count == 0)
                    "Хватит это делать!"
                else
                    "Можете выполнить еще ${habit.count} ${getDeclensionOfWord(habit.count)}"
            }
            HabitType.GOOD -> {
                if (habit.count == 0)
                    "You are breathtaking!"
                else
                    "Стоит выполнить еще ${habit.count} ${getDeclensionOfWord(habit.count)}"
            }
        }
    }

    private fun getDeclensionOfWord(number: Int): String
    {
        val div = number % 10

        return when {
            number in 10..20 -> "раз"
            div in 2..4 -> "раза"
            else -> "раз"
        }
    }
}