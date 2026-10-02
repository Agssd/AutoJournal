package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_records",
    foreignKeys = [
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ServiceRecordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val carId: Long,

    val title: String,

    val mileage: Int,

    val cost: Double,

    val date: Long
)