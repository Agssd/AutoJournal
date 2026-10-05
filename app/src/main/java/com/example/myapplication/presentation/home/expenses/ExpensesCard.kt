package com.example.myapplication.presentation.home.expenses

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExpensesCard(
    viewModel: ExpensesViewModel = koinViewModel(),
    onMenuClick: (route: String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Общие расходы", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                Text(
                    text = "${state.selectedYear}",
                    fontSize = 14.sp, color = TextGray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator(color = OrangePrimary, modifier = Modifier.size(28.dp))
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(formatRub(state.total), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.percentDelta?.let { String.format("%+.0f%% к прошлому году", it) }
                                ?: "Нет данных за прошлый год",
                            fontSize = 12.sp, color = OrangePrimary
                        )
                    }
                    ExpensesChart(items = state.monthly)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFF232733), thickness = 1.dp)

            ExpenseMenuItem(Icons.Outlined.Notifications, "Напоминания",
                badgeCount = state.remindersCount.takeIf { it > 0 },
                onClick = { onMenuClick("reminders") })
            ExpenseMenuItem(Icons.Outlined.AccountBalanceWallet, "Расходы",
                onClick = { onMenuClick("expenses_list") })
            ExpenseMenuItem(Icons.Outlined.BarChart, "Статистика",
                onClick = { onMenuClick("stats") })
            ExpenseMenuItem(Icons.Outlined.Description, "Документы",
                onClick = { onMenuClick("docs") }, showDivider = false)
        }
    }
}

private fun formatRub(v: Double): String {
    val f = NumberFormat.getNumberInstance(Locale("ru", "RU")).apply { maximumFractionDigits = 0 }
    return "${f.format(v)} ₽"
}

@Composable
private fun ExpensesChart(items: List<MonthlyExpenseUi>) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
        items.forEach { m ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .height((50.dp * m.ratio).coerceAtLeast(6.dp))
                        .width(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (m.isCurrent) OrangePrimary else ProgressTrack)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = m.label, fontSize = 9.sp, color = TextGray)
            }
        }
    }
}

@Composable
private fun ExpenseMenuItem(
    icon: ImageVector, title: String, badgeCount: Int? = null,
    showDivider: Boolean = true, onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, fontSize = 15.sp, color = TextWhite, modifier = Modifier.weight(1f))
            badgeCount?.let { count ->
                Box(
                    modifier = Modifier.size(22.dp).clip(CircleShape).background(OrangePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = count.toString(), color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray)
        }
        if (showDivider) HorizontalDivider(color = Color(0xFF232733), thickness = 0.5.dp)
    }
}