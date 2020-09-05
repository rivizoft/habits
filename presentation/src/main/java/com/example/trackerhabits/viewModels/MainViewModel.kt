package com.example.trackerhabits.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.data.utils.NetworkConnection
import com.example.domain.Habit
import com.example.domain.usecases.HabitIntercept
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application,
                    private val habitIntercept: HabitIntercept
) : AndroidViewModel(application)
{
    private val toastMessagesLiveData = MutableLiveData<String>()
    private val networkConnection: NetworkConnection
            = NetworkConnection(application.applicationContext)

    val toastMessages: LiveData<String> = toastMessagesLiveData

    fun syncLocalHabitsWithServer()
    {
        if (networkConnection.isOnline())
        {
            viewModelScope.launch()
            {
                withContext(Dispatchers.IO)
                {
                    //Сначала закачиваем из локалки на сервер
                    val habitsListFromLocalDB = habitIntercept.getListHabitsFromDb()
                    habitsListFromLocalDB.forEach { habit ->
                        if (habit.uid.isNullOrBlank())
                            uploadHabitToServerAndSetUid(habit)
                        if (habit.isDelete)
                        {
                            habitIntercept.deleteHabitFromDb(habit)
                            habitIntercept.deleteHabitFromServer(habit)
                        }
                        if (habit.isUpdated)
                        {
                            habitIntercept.addOrUpdateHabitToServer(habit)
                            habit.isUpdated = false
                            habitIntercept.updateHabitInDb(habit)
                        }
                    }

                    //теперь берем инфу с сервера и в локалку
                    val habitsListFromServer = habitIntercept.getListHabitsFromServer()
                    habitsListFromServer?.forEach { serverHabit ->
                        var found = false
                        for (localHabit in habitsListFromLocalDB) {
                            if (localHabit.uid.equals(serverHabit.uid)) {
                                found = true
                                break
                            }
                        }
                        if (!found)
                            habitIntercept.addHabitToDb(serverHabit)
                    }
                }
            }
        }
        else
            toastMessagesLiveData.value =
                "Не удается синхронизировать привычки, так как отсутсвует интернет!"
    }

    private suspend fun uploadHabitToServerAndSetUid(habit: Habit)
    {
        val resultUid = habitIntercept.addOrUpdateHabitToServer(habit)
        habit.uid = resultUid?.uid
        habitIntercept.updateHabitInDb(habit)
    }
}