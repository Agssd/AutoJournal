package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val carId: Long = 0,
    val type: String = "STS",
    val title: String = "",
    val fileUri: String = "",
    val mime: String = "",
    val createdAt: Long = System.currentTimeMillis()
)