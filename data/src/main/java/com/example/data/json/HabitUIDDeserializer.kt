package com.example.data.json

import com.example.domain.HabitUID
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitUIDDeserializer : JsonDeserializer<HabitUID>
{
    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?,
                             context: JsonDeserializationContext?): HabitUID?
    {
        if (json != null)
        {
            val jsonObject = json.asJsonObject
            val uid = jsonObject.get("uid").asString

            val habitUid = HabitUID(uid)
            return habitUid
        }

        return null
    }
}