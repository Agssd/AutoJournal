package com.example.myapplication.data.dao

import androidx.room.*
import com.example.myapplication.data.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    fun observe(): Flow<List<DocumentEntity>>

    @Insert
    suspend fun insert(e: DocumentEntity): Long

    @Delete
    suspend fun delete(e: DocumentEntity)
}