package com.example.myapplication.presentation.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.CarEntity
import com.example.myapplication.data.repository.CarRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AddEditState(
    val brand: String = "", val plate: String = "", val mileage: String = "",
    val vin: String = "", val year: String = "", val color: String = "",
    val saving: Boolean = false, val error: String? = null
) { val canSave get() = brand.isNotBlank() && plate.isNotBlank() && mileage.toIntOrNull() != null }

class AddEditCarViewModel(private val repo: CarRepository) : ViewModel() {
    private val _state = MutableStateFlow(AddEditState()); val state = _state.asStateFlow()
    private var editId: Long? = null

    fun init(id: String?) {
        editId = id?.toLongOrNull() ?: return
        viewModelScope.launch {
            repo.getCarById(editId!!)?.let { c ->
                _state.update {
                    it.copy(
                        brand = c.brand, plate = c.plate,
                        mileage = c.mileage.toString(),
                        vin = c.vin, year = c.year.takeIf { y -> y > 0 }?.toString() ?: ""
                    )
                }
            }
        }
    }
    fun onBrand(v: String) = _state.update { it.copy(brand = v) }
    fun onPlate(v: String) = _state.update { it.copy(plate = v) }
    fun onMileage(v: String) = _state.update { it.copy(mileage = v.filter(Char::isDigit)) }
    fun onVin(v: String) = _state.update { it.copy(vin = v) }
    fun onYear(v: String) = _state.update { it.copy(year = v.filter(Char::isDigit).take(4)) }
    fun onColor(v: String) = _state.update { it.copy(color = v) }
    fun save(done: () -> Unit) = viewModelScope.launch {
        val s = _state.value; if (!s.canSave) return@launch
        _state.update { it.copy(saving = true, error = null) }
        try {
            if (editId == null) {
                repo.insert(
                    CarEntity(
                        id = 0,
                        brand = s.brand.trim(),
                        model = "", // TODO: отдельное поле модели
                        year = s.year.toIntOrNull() ?: 0,
                        vin = s.vin.trim(),
                        plate = s.plate.trim().uppercase(),
                        mileage = s.mileage.toInt()
                    )
                )
            } else {
                repo.getCarById(editId!!)?.copy(
                    brand = s.brand.trim(),
                    plate = s.plate.trim().uppercase(),
                    mileage = s.mileage.toInt(),
                    vin = s.vin.trim(),
                    year = s.year.toIntOrNull() ?: 0
                )?.let { repo.update(it) }
            }
            done()
        } catch (t: Throwable) { _state.update { it.copy(saving = false, error = t.message) } }
    }
}