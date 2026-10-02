package com.example.myapplication.domain.model

data class Expense(
    val id: String,
    val amount: Double,
    val timestampMillis: Long,
    val category: String
)

data class MonthlyExpense(
    val monthIndex: Int,
    val label: String,
    val total: Double
)