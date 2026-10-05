package com.example.myapplication.presentation.expenses

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ExpensesRepository
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

data class Hist(val id: String, val date: String, val title: String, val amount: Double)
data class ExpScreenState(
    val period: Int = 2,
    val anchor: Long = System.currentTimeMillis(),
    val title: String = "2026",
    val total: Double = 0.0, val delta: Double? = null,
    val slices: List<Slice> = emptyList(), val history: List<Hist> = emptyList()
)

class ExpensesScreenViewModel(private val repo: ExpensesRepository) : ViewModel() {
    private val _period = MutableStateFlow(2)
    private val _anchor = MutableStateFlow(System.currentTimeMillis())
    val state: StateFlow<ExpScreenState> = combine(_period, _anchor, repo.observeAll()) { p, a, all ->
        val cal = Calendar.getInstance().apply { timeInMillis = a }
        val (start, end, title) = when (p) {
            0 -> {
                val c = (cal.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); clearTime()
                }
                val s = c.timeInMillis; c.add(Calendar.WEEK_OF_YEAR, 1)
                Triple(s, c.timeInMillis, "${fmtDay(s)} – ${fmtDay(c.timeInMillis - 1)}")
            }
            1 -> {
                val c = (cal.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1); clearTime() }
                val s = c.timeInMillis; c.add(Calendar.MONTH, 1)
                Triple(s, c.timeInMillis, monthYear(s))
            }
            2 -> {
                val c = (cal.clone() as Calendar).apply { set(Calendar.MONTH, 0); set(Calendar.DAY_OF_MONTH, 1); clearTime() }
                val s = c.timeInMillis; c.add(Calendar.YEAR, 1)
                Triple(s, c.timeInMillis, Calendar.getInstance().apply { timeInMillis = s }.get(Calendar.YEAR).toString())
            }
            else -> Triple(0L, Long.MAX_VALUE, "Всё время")
        }
        val curr = all.filter { it.timestamp in start until end }
        val len = end - start
        val prev = if (p == 3) emptyList() else all.filter { it.timestamp in (start - len) until start }
        val total = curr.sumOf { it.amount }
        val pt = prev.sumOf { it.amount }
        val colors = listOf(Color(0xFFFF8C1A), Color(0xFF4A90FF), Color(0xFF9B59B6), Color(0xFF2ECC71), Color(0xFF2C5F9E))
        val labels = mapOf("oil" to "ТО / Масло", "repair" to "Ремонт", "parts" to "Расходники", "tires" to "Шины", "fuel" to "Топливо", "other" to "Другие")
        val slices = curr.groupBy { it.category }.entries.mapIndexed { i, (c, l) ->
            val v = l.sumOf { it.amount }
            Slice(labels[c] ?: c, v, if (total > 0) (v / total * 100).toInt() else 0, colors[i % colors.size])
        }.sortedByDescending { it.value }
        val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val hist = curr.sortedByDescending { it.timestamp }.take(30).map {
            Hist(
                id = it.id,
                date = df.format(Date(it.timestamp)),
                title = it.title.ifEmpty { labels[it.category] ?: it.category },
                amount = it.amount
            )
        }
        ExpScreenState(p, a, title, total, if (pt > 0) (total - pt) / pt * 100 else null, slices, hist)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpScreenState())

    fun setPeriod(i: Int) { _period.value = i }
    fun shift(dir: Int) {
        val c = Calendar.getInstance().apply { timeInMillis = _anchor.value }
        when (_period.value) {
            0 -> c.add(Calendar.WEEK_OF_YEAR, dir)
            1 -> c.add(Calendar.MONTH, dir)
            2 -> c.add(Calendar.YEAR, dir)
        }
        _anchor.value = c.timeInMillis
    }

    private fun Calendar.clearTime() {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }
    private fun fmtDay(ts: Long) =
        SimpleDateFormat("dd MMM", Locale("ru")).format(Date(ts))
    private fun monthYear(ts: Long) =
        SimpleDateFormat("LLLL yyyy", Locale("ru")).format(Date(ts))
            .replaceFirstChar { it.uppercase() }
}