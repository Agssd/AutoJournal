package com.example.myapplication.presentation.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ExpensesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar


data class ExpensesUiState(
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val total: Double = 0.0,
    val prevYearTotal: Double = 0.0,
    val percentDelta: Double? = null,
    val monthly: List<MonthlyExpenseUi> = emptyList(),
    val remindersCount: Int = 0,
    val isLoading: Boolean = true
)

data class MonthlyExpenseUi(val label: String, val total: Double, val ratio: Float)

class ExpensesViewModel(
    private val repo: ExpensesRepository
) : ViewModel() {

    private val year = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    private val monthLabels = listOf("Янв","Фев","Мар","Апр","Май","Июн","Июл","Авг","Сен","Окт","Ноя","Дек")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ExpensesUiState> = year.flatMapLatest { y ->
        combine(
            repo.observeExpenses(y),
            repo.observeExpenses(y - 1),
            repo.observeRemindersCount()
        ) { curr, prev, reminders ->
            val total = curr.sumOf { it.amount }
            val prevTotal = prev.sumOf { it.amount }
            val delta = if (prevTotal > 0) (total - prevTotal) / prevTotal * 100 else null

            val byMonth = curr.groupBy {
                Calendar.getInstance().apply { timeInMillis = it.timestampMillis }
                    .get(Calendar.MONTH)
            }
            val max = (byMonth.values.maxOfOrNull { list -> list.sumOf { it.amount } } ?: 1.0)
            // показываем последние 6 месяцев года с данными, либо Янв-Июн если пусто
            val monthly = (0..5).map { m ->
                val sum = byMonth[m]?.sumOf { it.amount } ?: 0.0
                MonthlyExpenseUi(monthLabels[m], sum, if (max > 0) (sum / max).toFloat().coerceAtLeast(0.05f) else 0.05f)
            }

            ExpensesUiState(y, total, prevTotal, delta, monthly, reminders, false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpensesUiState())

    fun selectYear(y: Int) { year.value = y }
}