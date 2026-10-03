package com.example.myapplication.presentation.garage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.domain.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CarListState(
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = true
)

class GarageCarViewModel(
    private val repo: CarRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CarListState())
    val state: StateFlow<CarListState> = _state.asStateFlow()

    val cars: StateFlow<List<Car>> = repo.getCars()
        .onEach { _state.update { s -> s.copy(cars = it, isLoading = false) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(id: String) = viewModelScope.launch {
    }
}