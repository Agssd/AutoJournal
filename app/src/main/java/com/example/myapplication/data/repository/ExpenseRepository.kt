package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.ExpenseDao
import com.example.myapplication.data.entity.ExpenseEntity
import com.example.myapplication.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Calendar

interface ExpensesRepository {
    fun observeExpenses(year: Int): Flow<List<Expense>>
    fun observeRemindersCount(): Flow<Int>

    suspend fun insert(e: ExpenseEntity)

    fun observeAll(): Flow<List<Expense>>
}

class ExpensesRepositoryImpl(
    private val dao: ExpenseDao
) : ExpensesRepository {
    override fun observeExpenses(year: Int): Flow<List<Expense>> {
        val cal = Calendar.getInstance().apply {
            set(year, 0, 1, 0, 0, 0); set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        cal.add(Calendar.YEAR, 1)
        val end = cal.timeInMillis
        return dao.observeByRange(start, end).map { list ->
            list.map {
                Expense(
                    id = it.id.toString(),
                    amount = it.amount,
                    timestamp = it.timestamp,
                    category = it.category,
                    title = it.title,
                    description = it.description,
                    mileage = it.mileage,
                    carId = it.carId
                )
            }
        }
    }
    override fun observeRemindersCount(): Flow<Int> = flowOf(0)

    override suspend fun insert(e: ExpenseEntity) = dao.insert(e)

    override fun observeAll(): Flow<List<Expense>> = dao.observeAll().map { list ->
        list.map {
            Expense(
                id = it.id.toString(),
                amount = it.amount,
                timestamp = it.timestamp,
                category = it.category,
                title = it.title,
                description = it.description,
                mileage = it.mileage,
                carId = it.carId,
                photoUris = it.photoUris
            )
        }
    }
}