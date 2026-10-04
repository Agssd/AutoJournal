package com.example.myapplication.core.di

import androidx.room.Room
import com.example.myapplication.data.database.AutoJournalDatabase
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.data.repository.CarRepositoryImpl
import com.example.myapplication.data.repository.ExpensesRepository
import com.example.myapplication.data.repository.ExpensesRepositoryImpl
import com.example.myapplication.data.repository.ServiceRecordRepository
import com.example.myapplication.data.repository.ServiceRecordRepositoryImpl
import com.example.myapplication.presentation.cars.AddEditCarViewModel
import com.example.myapplication.presentation.garage.GarageCarViewModel
import com.example.myapplication.presentation.expenses.ExpensesViewModel
import com.example.myapplication.presentation.home.HomeViewModel
import com.example.myapplication.presentation.records.RecordScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AutoJournalDatabase::class.java,
            "auto_journal_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    viewModel { HomeViewModel(get()) }
    viewModel { ExpensesViewModel(get()) }
    viewModel { GarageCarViewModel(get()) }
    viewModel { AddEditCarViewModel(get()) }
    viewModel { RecordScreenViewModel(get(), get()) }

    single { get<AutoJournalDatabase>().carDao() }
    single { get<AutoJournalDatabase>().serviceRecordDao() }
    single { get<AutoJournalDatabase>().expenseDao() }

    single<ExpensesRepository> { ExpensesRepositoryImpl(dao = get()) }
    single<CarRepository> { CarRepositoryImpl(carDao = get()) }
    single<ServiceRecordRepository> { ServiceRecordRepositoryImpl(serviceRecordDao = get()) }
}