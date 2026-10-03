package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Query("SELECT * FROM cars")
    fun getCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getById(id: Long): CarEntity?

    @Insert
    suspend fun insert(car: CarEntity)

    @Delete
    suspend fun delete(car: CarEntity)

    @Update
    suspend fun update(car: CarEntity)
}