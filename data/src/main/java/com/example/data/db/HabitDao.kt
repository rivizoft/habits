package com.example.data.db

import androidx.room.*
import com.example.data.mapping.DbHabit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addHabit(dbHabit: DbHabit)

    @Update
    fun updateHabit(dbHabit: DbHabit)

    @Delete
    fun deleteHabit(dbHabit: DbHabit)

    @Query("SELECT * FROM habits_table ORDER BY id ASC")
    fun allLiveData(): Flow<List<DbHabit>>

    @Query("SELECT * FROM habits_table")
    fun getAllData(): List<DbHabit>

    @Query("SELECT * FROM habits_table WHERE title LIKE '%' || :title || '%'")
    fun getAllHabitsWithTitleLike(title: String): List<DbHabit>
}