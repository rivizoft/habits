package com.example.data.server.api

import com.example.domain.Habit
import com.example.domain.HabitUID
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DoubletappApi
{
    @GET("habit")
    fun getHabits(): Call<List<com.example.domain.Habit>>

    @PUT("habit")
    fun addHabit(@Body habit: com.example.domain.Habit): Call<HabitUID>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    fun deleteHabit(@Body habitUID: HabitUID): Call<ResponseBody>

//    @POST("habit_done")
//    fun doneHabit()
}