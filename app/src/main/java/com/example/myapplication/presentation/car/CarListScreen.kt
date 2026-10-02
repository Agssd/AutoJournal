package com.example.myapplication.presentation.car

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun CarListScreen(
    viewModel: CarViewModel = koinViewModel()
) {
    val cars by viewModel.cars.collectAsState()

    // Отображаем список машин в LazyColumn
}