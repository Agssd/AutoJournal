package com.example.myapplication.presentation.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.CardBg
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.ProgressTrack
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite

@Composable
fun ExpensesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Заголовок карточки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Общие расходы",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Text(
                    text = "За год ⌄",
                    fontSize = 14.sp,
                    color = TextGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Сумма и Мини-график
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "18 750 €",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+12% к прошлому году",
                        fontSize = 12.sp,
                        color = OrangePrimary
                    )
                }

                // Простой гистограммный график
                ExpensesChart()
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFF232733), thickness = 1.dp)

            // Быстрые пункты меню
            ExpenseMenuItem(
                icon = Icons.Outlined.Notifications,
                title = "Напоминания",
                badgeCount = 3,
                onClick = {}
            )
            ExpenseMenuItem(
                icon = Icons.Outlined.AccountBalanceWallet,
                title = "Расходы",
                onClick = {}
            )
            ExpenseMenuItem(
                icon = Icons.Outlined.BarChart,
                title = "Статистика",
                onClick = {}
            )
            ExpenseMenuItem(
                icon = Icons.Outlined.Description,
                title = "Документы",
                onClick = {},
                showDivider = false
            )
        }
    }
}

@Composable
private fun ExpensesChart() {
    val months = listOf("Янв", "Фев", "Мар", "Апр", "Май", "Июн")
    val heights = listOf(0.4f, 0.6f, 0.5f, 0.7f, 0.6f, 1.0f)

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        months.forEachIndexed { index, month ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .height(50.dp * heights[index])
                        .width(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (index == 5) OrangePrimary else ProgressTrack)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = month, fontSize = 9.sp, color = TextGray)
            }
        }
    }
}

@Composable
private fun ExpenseMenuItem(
    icon: ImageVector,
    title: String,
    badgeCount: Int? = null,
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextGray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                color = TextWhite,
                modifier = Modifier.weight(1f)
            )

            badgeCount?.let { count ->
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(OrangePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextGray
            )
        }
        if (showDivider) {
            HorizontalDivider(color = Color(0xFF232733), thickness = 0.5.dp)
        }
    }
}