package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.ServiceRecordEntity
import com.example.myapplication.domain.model.ServiceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRecordDao {
    @Query("SELECT * FROM service_records WHERE carId = :carId ORDER BY date DESC")
    fun getRecordsForCar(carId: Long): Flow<List<ServiceRecord>>

    @Insert
    suspend fun insert(record: ServiceRecordEntity)

    @Update
    suspend fun update(record: ServiceRecordEntity)

    @Delete
    suspend fun delete(record: ServiceRecordEntity)
}