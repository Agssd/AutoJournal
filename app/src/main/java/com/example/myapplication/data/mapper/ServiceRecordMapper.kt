package com.example.myapplication.data.mapper

import com.example.myapplication.data.entity.ServiceRecordEntity
import com.example.myapplication.domain.model.ServiceRecord

fun ServiceRecordEntity.toDomain() = ServiceRecord(
    id = id,
    carId = carId,
    title = title,
    mileage = mileage,
    cost = cost,
    date = date
)

fun ServiceRecord.toEntity() = ServiceRecordEntity(
    id = id,
    carId = carId,
    title = title,
    mileage = mileage,
    cost = cost,
    date = date
)