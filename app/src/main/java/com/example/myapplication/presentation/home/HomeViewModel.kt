package com.example.myapplication.presentation.home

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.CarRepository
import com.example.myapplication.domain.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val repository: CarRepository
) : ViewModel() {

    val mockCars = listOf(
        Car(
            id = "1",
            brand = "BMW 320d",
            plate = "AA 1234 AB",
            mileage = 245300,
            isFavorite = true,
            maintenanceType = Car.MaintenanceType.OIL_CHANGE,
            nextMaintenanceMileage = 248800 // 248800 - 245300 = 3500 км осталось
        ),
        Car(
            id = "2",
            brand = "Volkswagen Polo",
            plate = "KA 5678 KB",
            mileage = 134100,
            isFavorite = false,
            maintenanceType = Car.MaintenanceType.INSPECTION,
            monthsUntilMaintenance = 2
        )
    )

    private val _state = MutableStateFlow(
        HomeState(
            cars = mockCars
        )
    )

    val state = _state.asStateFlow()

}