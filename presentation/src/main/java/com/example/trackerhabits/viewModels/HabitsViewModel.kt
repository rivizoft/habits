package com.example.trackerhabits.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.domain.Habit
import com.example.domain.HabitType
import com.example.data.utils.NetworkConnection
import com.example.domain.usecases.HabitIntercept

class HabitsViewModel(application: Application,
                      private val habitIntercept: HabitIntercept,
                      private val typeOfHabitsReturn: HabitType) : AndroidViewModel(application)
{
    private val sortedAllData: MutableLiveData<List<Habit>> = MutableLiveData()
    private val toastMessages: MutableLiveData<String> = MutableLiveData()

    private val allHabits: LiveData<List<Habit>> = habitIntercept.getAllHabitsFromDb().asLiveData()

    private val typeOfListener: HabitType = typeOfHabitsReturn
    private var cachedListHabits: List<Habit> = listOf()

    private val networkConnection: NetworkConnection

    val allHabitsLiveData: LiveData<List<Habit>> = sortedAllData
    val toastMessagesData: LiveData<String> = toastMessages

    init
    {
        allHabits.observeForever()
        {
            cachedListHabits = getSortedList(it)
            sortedAllData.value = cachedListHabits
        }

        networkConnection = NetworkConnection(application.applicationContext)
    }

    fun getListOfHabitsFromQuery(query: String): List<Habit>
    {
        if (query.isEmpty())
            return cachedListHabits
        return getSortedList(habitIntercept.getAllHabitsFromDbWithTitleLike(query))
    }

    fun doneHabit(habit: Habit)
    {
        val message = habitIntercept.doneHabitAndUpdateInBd(habit)
        toastMessages.value = message
    }

    private fun getSortedList(habits: List<Habit>): List<Habit>
    {
        val tempList = arrayListOf<Habit>()

        for (habit in habits)
            if (habit.type == typeOfListener && !habit.isDelete)
                tempList.add(habit)

        return tempList.sortedWith(compareBy { it.priority })
    }
}