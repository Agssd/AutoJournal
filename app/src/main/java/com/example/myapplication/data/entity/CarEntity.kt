package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val vin: String,
    val plate: String,
    val mileage: Int,
    val color: String = "",
    val isFavorite: Boolean = false,
    val nextOilChangeMileage: Int = 3500
)