package com.example.myapplication.core.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home")

    object CarList : Screen("car_list")

    data object Garage : Screen("garage")

    data object AddRecord : Screen("add_record")

    data object Expenses : Screen("expenses")

    data object Docs : Screen("docs")
}