package com.example.myapplication.presentation.records

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.ExpenseEntity
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.data.repository.ExpensesRepository
import com.example.myapplication.domain.model.Car
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

data class RecordState(
    val type: String = "oil",
    val date: String = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
        .format(java.util.Date()),
    val mileage: String = "245 300",
    val title: String = "Замена масла",
    val desc: String = "",
    val cost: String = "128",
    val extra1: String = "",
    val extra2: String = "",
    val saving: Boolean = false,
    val error: String? = null
)

class RecordScreenViewModel(
    private val repo: ExpensesRepository,
    carRepo: CarRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RecordState())
    val state: StateFlow<RecordState> = _state.asStateFlow()

    val cars: StateFlow<List<Car>> = carRepo.getCars()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _carId = MutableStateFlow<Long?>(null)
    val selectedCarId: StateFlow<Long?> = _carId.asStateFlow()
    fun selectCar(id: Long) = _carId.update { id }

    private val _photos = MutableStateFlow<List<Uri>>(emptyList())
    val photos = _photos.asStateFlow()
    fun addPhoto(u: Uri) = _photos.update { it + u }

    fun setType(v: String) = _state.update {
        it.copy(type = v, title = "", desc = "", extra1 = "", extra2 = "", cost = "")
    }
    fun setDate(v: String) = _state.update { it.copy(date = v) }
    fun setMileage(v: String) = _state.update { it.copy(mileage = v) }
    fun setTitle(v: String) = _state.update { it.copy(title = v) }
    fun setDesc(v: String) = _state.update { it.copy(desc = v) }
    fun setCost(v: String) = _state.update { it.copy(cost = v) }
    fun setExtra1(v: String) = _state.update { it.copy(extra1 = v) }
    fun setExtra2(v: String) = _state.update { it.copy(extra2 = v) }

    fun save(ctx: Context, done: () -> Unit) = viewModelScope.launch {
        if (cars.value.isEmpty()) {
            _state.update { it.copy(error = "Добавьте автомобиль") }; return@launch
        }
        val carId = _carId.value
        if (carId == null) {
            _state.update { it.copy(error = "Выберите автомобиль") }; return@launch
        }
        val s = _state.value
        val amount = s.cost.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
        if (amount <= 0) { _state.update { it.copy(error = "Укажите стоимость") }; return@launch }
        _state.update { it.copy(saving = true, error = null) }
        try {
            val ts = try {
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(s.date)?.time
                    ?: System.currentTimeMillis()
            } catch (_: Exception) { System.currentTimeMillis() }

            val savedUris = _photos.value.mapNotNull { uri ->
                try {
                    val f = java.io.File(ctx.filesDir, "cars/photo_${System.currentTimeMillis()}_${uri.lastPathSegment?.takeLast(8)}.jpg")
                    f.parentFile?.mkdirs()
                    ctx.contentResolver.openInputStream(uri)?.use { ins ->
                        FileOutputStream(f).use { ins.copyTo(it) }
                    }
                    androidx.core.content.FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", f).toString()
                } catch (_: Exception) { null }
            }.joinToString(";")

            repo.insert(
                ExpenseEntity(
                    amount = amount,
                    timestamp = ts,
                    category = s.type,
                    title = s.title.trim(),
                    description = s.desc.trim(),
                    mileage = s.mileage.replace(" ", "").toIntOrNull() ?: 0,
                    carId = carId,
                    photoUris = savedUris
                )
            )
            done()
        } catch (t: Throwable) {
            _state.update { it.copy(saving = false, error = t.message) }
        }
    }
}