package com.example.myapplication.presentation.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.CarEntity
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.domain.model.Car
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarViewModel(
    private val carRepository: CarRepository
) : ViewModel() {

    // Автоматически обновляемый поток списка машин
    val cars: StateFlow<List<Car>> = carRepository.getCars()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCar(car: CarEntity) {
        viewModelScope.launch {
            carRepository.insert(car)
        }
    }
}