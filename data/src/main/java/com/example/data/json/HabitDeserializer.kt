package com.example.data.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitDeserializer : JsonDeserializer<com.example.domain.Habit>
{
    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?,
                             context: JsonDeserializationContext?): com.example.domain.Habit?
    {
        if (json != null)
        {
            val jsonHabit = json.asJsonObject
                val uid = jsonHabit.get("uid").asString
                val title = jsonHabit.get("title").asString
                val description = jsonHabit.get("description").asString
                val priority = jsonHabit.get("priority").asInt
                val count = jsonHabit.get("count").asInt
                val frequency = jsonHabit.get("frequency").asInt
                val color = jsonHabit.get("color").asInt
                val date = jsonHabit.get("date").asLong

                val type = if (jsonHabit.get("type").asInt == 0)
                    com.example.domain.HabitType.GOOD else com.example.domain.HabitType.BAD

                val habit = com.example.domain.Habit(
                    0, uid, title, description, color,
                    type, priority, frequency, date, count
                )

                return habit
        }

        return null
    }

}