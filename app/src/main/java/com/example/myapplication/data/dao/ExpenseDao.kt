package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE timestamp >= :start AND timestamp < :end ORDER BY timestamp DESC")
    fun observeByRange(start: Long, end: Long): Flow<List<ExpenseEntity>>

    @Insert
    suspend fun insert(e: ExpenseEntity)

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun count(): Int

}