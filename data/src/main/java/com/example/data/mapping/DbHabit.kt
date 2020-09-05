package com.example.data.mapping

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.db.converters.HabitTypeConverter
import com.example.domain.HabitType
import java.io.Serializable

@Entity(tableName = "habits_table")
data class DbHabit (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var uid: String? = null,
    val title: String,
    val describe: String,
    val color: Int,
    @TypeConverters(HabitTypeConverter::class)
    val type: HabitType,
    val priority: Int,
    val frequency: Int,
    val date: Long,
    val count: Int,
    var isDelete: Boolean,
    var isUpdated: Boolean
) : Serializable