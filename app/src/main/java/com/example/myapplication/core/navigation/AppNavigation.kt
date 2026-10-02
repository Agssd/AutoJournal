package com.example.myapplication.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.components.MainScaffold
import com.example.myapplication.presentation.home.HomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    MainScaffold { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {

            composable(Screen.Home.route) {
                HomeScreen(padding)
            }

        }

    }

}