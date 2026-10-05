package com.example.myapplication.presentation.home.expenses

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

data class MonthlyExpenseUi(
    val label: String,
    val total: Double,
    val ratio: Float,
    val isCurrent: Boolean = false
)

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

            val now = Calendar.getInstance()
            val curYear = now.get(Calendar.YEAR)
            val curMonth = now.get(Calendar.MONTH)
            val endMonth = if (y == curYear) curMonth else 11

            val cal = Calendar.getInstance()
            fun sumFor(year: Int, month: Int): Double {
                val src = if (year == y) curr else prev
                return src.filter {
                    cal.apply { timeInMillis = it.timestamp }
                    cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
                }.sumOf { it.amount }
            }

            val pairs = (5 downTo 0).map { back ->
                var yy = y
                var mm = endMonth - back
                if (mm < 0) { mm += 12; yy -= 1 }
                yy to mm
            }
            val totals = pairs.map { (yy, mm) -> sumFor(yy, mm) }
            val max = totals.maxOrNull()?.takeIf { it > 0 } ?: 1.0

            val monthly = pairs.mapIndexed { i, (yy, mm) ->
                MonthlyExpenseUi(
                    label = monthLabels[mm],
                    total = totals[i],
                    ratio = (totals[i] / max).toFloat().coerceAtLeast(0.05f),
                    isCurrent = yy == curYear && mm == curMonth
                )
            }

            ExpensesUiState(y, total, prevTotal, delta, monthly, reminders, false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpensesUiState())

    fun selectYear(y: Int) { year.value = y }
}