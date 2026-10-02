package com.example.myapplication.domain.model

data class ServiceRecord(
    val id: Long,
    val carId: Long,
    val title: String,
    val mileage: Int,
    val cost: Double,
    val date: Long
)