package com.example.myapplication.data.mapper

import com.example.myapplication.data.entity.CarEntity
import com.example.myapplication.domain.model.Car

fun CarEntity.toDomain(): Car {
    return Car(
        id = id.toString(),
        brand = "$brand $model",
        plate = plate,
        mileage = mileage,
        isFavorite = isFavorite,
        maintenanceType = Car.MaintenanceType.OIL_CHANGE,
        nextMaintenanceMileage = nextOilChangeMileage,
        monthsUntilMaintenance = null
    )
}

fun Car.toEntity(): CarEntity {
    val brandParts = brand.split(" ", limit = 2)
    val brandName = brandParts.getOrNull(0) ?: brand
    val modelName = brandParts.getOrNull(1) ?: ""

    return CarEntity(
        id = id.toLongOrNull() ?: 0L,
        brand = brandName,
        model = modelName,
        year = 2020,
        vin = "",
        plate = plate,
        mileage = mileage,
        isFavorite = isFavorite,
        nextOilChangeMileage = nextMaintenanceMileage ?: 0
    )
}