package com.example.domain

import java.io.Serializable

enum class HabitType(val value: Int): Serializable
{
    GOOD(0),
    BAD(1)
}