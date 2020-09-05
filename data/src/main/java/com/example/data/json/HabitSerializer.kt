package com.example.data.json

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitSerializer : JsonSerializer<com.example.domain.Habit>
{
    override fun serialize(src: com.example.domain.Habit?,
                           typeOfSrc: Type?,
                           context: JsonSerializationContext?): JsonElement
    {
        if (src != null)
        {
            val json = JsonObject().apply()
            {
                addProperty("color", src.color)
                addProperty("count", src.count)
                addProperty("date", src.date)
                addProperty("description", src.describe)
                addProperty("frequency", src.frequency)
                addProperty("priority", src.priority)
                addProperty("title", src.title)
                addProperty("type", src.type.value)
                if (src.uid != null)
                    addProperty("uid", src.uid)
            }
            return json
        }

        return JsonObject()
    }
}