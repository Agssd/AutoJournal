package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.ServiceRecordDao
import com.example.myapplication.data.entity.ServiceRecordEntity
import com.example.myapplication.domain.model.ServiceRecord
import kotlinx.coroutines.flow.Flow

interface ServiceRecordRepository {
    fun getRecordsForCar(carId: Long): Flow<List<ServiceRecord>>
    suspend fun insert(record: ServiceRecordEntity)
    suspend fun update(record: ServiceRecordEntity)
    suspend fun delete(record: ServiceRecordEntity)
}

class ServiceRecordRepositoryImpl(
    private val serviceRecordDao: ServiceRecordDao
) : ServiceRecordRepository {

    override fun getRecordsForCar(carId: Long): Flow<List<ServiceRecord>> {
        return serviceRecordDao.getRecordsForCar(carId)
    }

    override suspend fun insert(record: ServiceRecordEntity) = serviceRecordDao.insert(record)

    override suspend fun update(record: ServiceRecordEntity) = serviceRecordDao.update(record)

    override suspend fun delete(record: ServiceRecordEntity) = serviceRecordDao.delete(record)
}