package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val carId: Long = 0,
    val amount: Double,
    val timestamp: Long,
    val category: String,
    val title: String = "",
    val description: String = "",
    val photoUris: String = "",
    val mileage: Int = 0
)