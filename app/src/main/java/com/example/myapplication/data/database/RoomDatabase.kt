package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.CarDao
import com.example.myapplication.data.dao.ServiceRecordDao
import com.example.myapplication.data.entity.CarEntity
import com.example.myapplication.data.entity.ServiceRecordEntity

@Database(
    entities = [
        CarEntity::class,
        ServiceRecordEntity::class
    ],
    version = 1
)
abstract class AutoJournalDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    abstract fun serviceRecordDao(): ServiceRecordDao

}