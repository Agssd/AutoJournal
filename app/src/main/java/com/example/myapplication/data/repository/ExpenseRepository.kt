package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.ExpenseDao
import com.example.myapplication.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Calendar

interface ExpensesRepository {
    fun observeExpenses(year: Int): Flow<List<Expense>>
    fun observeRemindersCount(): Flow<Int>
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
            list.map { Expense(it.id.toString(), it.amount, it.timestamp, it.category) }
        }
    }
    override fun observeRemindersCount(): Flow<Int> = flowOf(0)
}