package com.example.data.server

import com.example.data.json.HabitDeserializer
import com.example.data.json.HabitSerializer
import com.example.data.json.HabitUIDDeserializer
import com.example.data.json.HabitUIDSerializer
import com.example.data.server.api.DoubletappApi
import com.example.domain.constants.ApiConstants
import com.example.domain.HabitUID
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabitServer
{
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(CustomInterceptor())
    }.build()

    private val gson = GsonBuilder().apply {
        registerTypeAdapter(com.example.domain.Habit::class.java, HabitSerializer())
        registerTypeAdapter(com.example.domain.Habit::class.java, HabitDeserializer())
        registerTypeAdapter(HabitUID::class.java, HabitUIDSerializer())
        registerTypeAdapter(HabitUID::class.java, HabitUIDDeserializer())
        excludeFieldsWithoutExposeAnnotation()
    }.create()

    private val retrofit by lazy()
    {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: DoubletappApi by lazy()
    {
        retrofit.create(DoubletappApi::class.java)
    }
}