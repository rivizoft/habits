package com.example.trackerhabits

import java.io.Serializable

data class Habit (
    val title: String,
    val describe: String,
    val color: Int
) : Serializable