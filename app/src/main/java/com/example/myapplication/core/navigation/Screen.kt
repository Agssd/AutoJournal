package com.example.myapplication.core.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home")

    data object Garage : Screen("garage")

    data object AddRecord : Screen("add_record")

    data object Expenses : Screen("expenses")

    data object Settings : Screen("settings")
}