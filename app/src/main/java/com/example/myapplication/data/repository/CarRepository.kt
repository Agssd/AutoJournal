package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.CarDao
import com.example.myapplication.data.entity.CarEntity
import com.example.myapplication.domain.model.Car
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CarRepository {
    fun getCars(): Flow<List<Car>>
    suspend fun insert(car: CarEntity)
    suspend fun update(car: CarEntity)
    suspend fun getCarById(id: Long): CarEntity?
    suspend fun delete(car: CarEntity)
}

class CarRepositoryImpl(
    private val carDao: CarDao
) : CarRepository {

    override fun getCars(): Flow<List<Car>> {
        return carDao.getCars().map { entities ->
            entities.map { e ->
                Car(
                    id = e.id.toString(),
                    brand = "${e.brand} ${e.model}".trim(),
                    plate = e.plate,
                    mileage = e.mileage,
                    isFavorite = e.isFavorite,
                    maintenanceType = Car.MaintenanceType.OIL_CHANGE,
                    nextMaintenanceMileage = e.nextOilChangeMileage,
                    monthsUntilMaintenance = null,
                    vin = e.vin,
                    year = e.year,
                    color = e.color
                )
            }
        }
    }

    override suspend fun insert(car: CarEntity) = carDao.insert(car)
    override suspend fun update(car: CarEntity) = carDao.update(car)
    override suspend fun getCarById(id: Long): CarEntity? = carDao.getById(id)
    override suspend fun delete(car: CarEntity) = carDao.delete(car)
}