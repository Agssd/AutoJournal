package com.example.myapplication.presentation.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.CardBg
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite
import com.example.myapplication.ui.theme.*

@Composable
fun MainScaffold(
    content: @Composable (PaddingValues) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            Surface(
                color = CardBg,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Home,
                        label = "Главная",
                        isSelected = selectedIndex == 0
                    ) { selectedIndex = 0 }

                    BottomItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.DirectionsCar,
                        label = "Автобили".let { "Автомобили" },
                        isSelected = selectedIndex == 1
                    ) { selectedIndex = 1 }

                    // Центральная кнопка — занимает 1/5 ширины, сама по центру слота
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(OrangePrimary)
                                .clickable { /* Действие при клике на плюс */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Добавить",
                                tint = TextWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    BottomItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.AccountBalanceWallet,
                        label = "Расходы",
                        isSelected = selectedIndex == 2
                    ) { selectedIndex = 2 }

                    BottomItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.MoreHoriz,
                        label = "Ещё",
                        isSelected = selectedIndex == 3
                    ) { selectedIndex = 3 }
                }
            }
        }
    ) { padding ->
        content(padding)
    }
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