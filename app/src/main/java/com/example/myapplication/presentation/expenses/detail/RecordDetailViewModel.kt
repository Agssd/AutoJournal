package com.example.myapplication.presentation.records

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.ExpenseEntity
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.data.repository.ExpensesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class DetailState(
    val loading: Boolean = true,
    val expense: ExpenseEntity? = null,
    val typeLabel: String = "",
    val carLabel: String = "",
    val date: String = "",
    val mileage: String = "",
    val cost: String = "",
    val photos: List<Uri> = emptyList()
)

class RecordDetailViewModel(
    private val expenseRepo: ExpensesRepository,
    private val carRepo: CarRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()
    private val labels = mapOf("oil" to "ТО / Масло", "repair" to "Ремонт", "parts" to "Расходники",
        "tires" to "Шины", "fuel" to "Топливо", "other" to "Другие")

    fun load(id: String) = viewModelScope.launch {
        val eid = id.toLongOrNull() ?: return@launch
        expenseRepo.observeAll().map { l -> l.find { it.id.toString() == id } }.collect { e ->
            e ?: return@collect
            _state.value = DetailState(
                loading = false,
                expense = ExpenseEntity(
                    id = eid,
                    carId = e.carId,
                    amount = e.amount,
                    timestamp = e.timestamp,
                    category = e.category,
                    title = e.title,
                    description = e.description,
                    photoUris = e.photoUris,
                    mileage = e.mileage
                ),
                typeLabel = labels[e.category] ?: e.category,
                photos = e.photoUris.split(";").filter { it.isNotBlank() }.map { Uri.parse(it) },
                carLabel = "Авто #${e.carId}",
                date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(e.timestamp)),
                mileage = if (e.mileage > 0) "%,d км".format(e.mileage).replace(",", " ") else "",
                cost = NumberFormat.getNumberInstance(Locale("ru", "RU")).apply { maximumFractionDigits = 0 }.format(e.amount)
            )
        }
    }
}