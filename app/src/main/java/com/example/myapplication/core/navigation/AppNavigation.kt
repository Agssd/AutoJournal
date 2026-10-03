package com.example.myapplication.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.cars.GarageCar
import com.example.myapplication.presentation.MainScaffold
import com.example.myapplication.presentation.car.AddEditCarScreen
import com.example.myapplication.presentation.home.HomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    MainScaffold(navController) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {

            composable(Screen.Home.route) { HomeScreen(padding, navController) }
            composable(Screen.CarList.route) {
                GarageCar(
                    padding = padding,
                    onBack = { navController.popBackStack() },
                    onAddCar = { navController.navigate("car_add") },
                    onCarClick = { id -> navController.navigate("car_detail/$id") },
                    onCarEdit = { id -> navController.navigate("car_edit/$id") }
                )
            }

            composable("car_add") { AddEditCarScreen(padding = padding, onBack = { navController.popBackStack() }) }
            composable("car_edit/{id}") {
                AddEditCarScreen(
                    carId = it.arguments?.getString("id"),
                    padding = padding,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Expenses.route) { /* ExpensesScreen\*/ }
            composable(Screen.Settings.route) { /* MoreScreen */ }
            composable(Screen.AddRecord.route) { /* AddCarScreen */ }
        }
    }
}