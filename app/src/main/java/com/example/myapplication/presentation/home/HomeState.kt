package com.example.myapplication.presentation.home

import com.example.myapplication.domain.model.Car

data class HomeState(
    val isLoading: Boolean = false,
    val cars: List<Car> = emptyList(),
    val totalExpenses: Double = 0.0
)