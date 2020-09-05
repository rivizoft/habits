package com.example.domain

import androidx.room.Entity
import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.Serializable

data class Habit (
    var id: Int,
    var uid: String? = null,
    val title: String,
    val describe: String,
    val color: Int,
    val type: HabitType,
    val priority: Int,
    val frequency: Int,
    val date: Long,
    var count: Int,
    var isDelete: Boolean = false,
    var isUpdated: Boolean = false
) : Serializable