package com.example.data.json

import com.example.domain.HabitUID
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitUIDSerializer : JsonSerializer<HabitUID>
{
    override fun serialize(src: HabitUID?,
                           typeOfSrc: Type?,
                           context: JsonSerializationContext?): JsonElement
    {
        if (src != null)
        {
            val json = JsonObject().apply {
                addProperty("uid", src.uid)
            }
            return json
        }

        return JsonObject()
    }

}