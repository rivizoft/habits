package com.example.trackerhabits.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Habit
import com.example.data.utils.NetworkConnection
import com.example.domain.usecases.HabitIntercept
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddOrEditHabitViewModel(application: Application,
                              private val habitIntercept: HabitIntercept
) : AndroidViewModel(application)
{
    private val networkConnection: NetworkConnection
            = NetworkConnection(application.applicationContext)

    fun addHabit(habit: Habit)
    {
        viewModelScope.launch()
        {
            withContext(Dispatchers.IO)
            {
                if (networkConnection.isOnline())
                    sendHabitToServerAndUpdateUid(habit)
                else
                    habitIntercept.addHabitToDb(habit)
            }
        }
    }

    fun updateHabit(newHabit: Habit)
    {
        viewModelScope.launch()
        {
            withContext(Dispatchers.IO)
            {
                habitIntercept.updateHabitInDb(newHabit)
                if (networkConnection.isOnline())
                    habitIntercept.addOrUpdateHabitToServer(newHabit)
                else
                    newHabit.isUpdated = true
                habitIntercept.updateHabitInDb(newHabit)
            }
        }
    }

    fun deleteHabit(habit: Habit)
    {
        viewModelScope.launch()
        {
            withContext(Dispatchers.IO)
            {
                if (networkConnection.isOnline())
                {
                    habitIntercept.deleteHabitFromServer(habit)
                    habitIntercept.deleteHabitFromDb(habit)
                }
                else
                {
                    habit.isDelete = true
                    habitIntercept.updateHabitInDb(habit)
                }
            }
        }
    }

    private suspend fun sendHabitToServerAndUpdateUid(habit: Habit)
    {
        val resultUid = habitIntercept.addOrUpdateHabitToServer(habit)
        habit.uid = resultUid?.uid
        habitIntercept.addHabitToDb(habit)
    }
}