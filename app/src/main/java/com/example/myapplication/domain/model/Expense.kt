package com.example.myapplication.domain.model

data class Expense(
    val id: String,
    val amount: Double,
    val timestamp: Long,
    val category: String,
    val title: String = "",
    val description: String = "",
    val mileage: Int = 0,
    val carId: Long = 0,
    val photoUris: String = ""
)