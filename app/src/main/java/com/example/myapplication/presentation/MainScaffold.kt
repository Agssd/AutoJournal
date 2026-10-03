package com.example.myapplication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.core.navigation.Screen
import com.example.myapplication.core.theme.CardBg
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite

@Composable
fun MainScaffold(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {

    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val selectedIndex = when (route) {
        Screen.Home.route -> 0
        Screen.CarList.route -> 1
        Screen.Expenses.route -> 2
        else -> 3
    }
    fun navigate(r: String) {
        navController.navigate(r) {
            popUpTo(Screen.Home.route) { saveState = true }
            launchSingleTop = true; restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            Surface(color = CardBg, tonalElevation = 8.dp, modifier = Modifier.navigationBarsPadding()) {
                Row(Modifier.fillMaxWidth().height(70.dp), verticalAlignment = Alignment.CenterVertically) {
                    BottomItem(Modifier.weight(1f), Icons.Outlined.Home, "Главная", selectedIndex == 0)
                    { navigate(Screen.Home.route) }

                    BottomItem(Modifier.weight(1f), Icons.Outlined.DirectionsCar, "Автомобили", selectedIndex == 1)
                    { navigate(Screen.CarList.route) }

                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Box(Modifier.size(52.dp).clip(CircleShape).background(OrangePrimary)
                            .clickable { navigate("car_add") }, contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, null, tint = TextWhite, modifier = Modifier.size(28.dp))
                        }
                    }

                    BottomItem(Modifier.weight(1f), Icons.Outlined.AccountBalanceWallet, "Расходы", selectedIndex == 2)
                    { navigate(Screen.Expenses.route) }

                    BottomItem(Modifier.weight(1f), Icons.Outlined.MoreHoriz, "Ещё", selectedIndex == 3)
                    { navigate(Screen.Settings.route) }
                }
            }
        }
    ) { padding -> content(padding) }
}

@Composable
private fun BottomItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) OrangePrimary else TextGray
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = color)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 10.sp, color = color)
    }
}