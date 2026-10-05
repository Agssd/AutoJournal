package com.example.myapplication.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.core.navigation.Screen
import com.example.myapplication.core.theme.DarkBg
import com.example.myapplication.presentation.home.components.CarCard
import com.example.myapplication.presentation.home.components.EmptyGarageCard
import com.example.myapplication.presentation.home.components.HomeTopBar
import com.example.myapplication.presentation.home.expenses.ExpensesCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    padding: PaddingValues,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeTopBar()
        }

        if (state.cars.isEmpty()) {
            item { EmptyGarageCard(onAddClick = { navController.navigate(Screen.Garage.route) }) }
        } else {
            items(state.cars, key = { it.id }) { car -> CarCard(car = car) }
        }

        item {
            ExpensesCard(
                onMenuClick = { route -> navController.navigate(route) }
            )
        }
    }
}