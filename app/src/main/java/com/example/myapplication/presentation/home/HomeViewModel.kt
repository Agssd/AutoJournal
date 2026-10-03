package com.example.myapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.domain.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CarRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(cars = emptyList()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCars().collect { cars ->
                _state.update { it.copy(cars = cars) }
            }
        }
    }
}