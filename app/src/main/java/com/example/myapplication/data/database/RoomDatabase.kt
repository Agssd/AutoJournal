package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.CarDao
import com.example.myapplication.data.dao.DocumentDao
import com.example.myapplication.data.dao.ExpenseDao
import com.example.myapplication.data.dao.ServiceRecordDao
import com.example.myapplication.data.entity.CarEntity
import com.example.myapplication.data.entity.DocumentEntity
import com.example.myapplication.data.entity.ExpenseEntity
import com.example.myapplication.data.entity.ServiceRecordEntity

@Database(
    entities = [
        CarEntity::class,
        ServiceRecordEntity::class,
        ExpenseEntity::class,
        DocumentEntity::class
    ],
    version = 5
)
abstract class AutoJournalDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    abstract fun serviceRecordDao(): ServiceRecordDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun documentDao(): DocumentDao

}