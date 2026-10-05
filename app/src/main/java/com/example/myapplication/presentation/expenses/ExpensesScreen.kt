package com.example.myapplication.presentation.expenses

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.core.theme.*
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExpensesScreen(
    padding: PaddingValues,
    viewModel: ExpensesScreenViewModel = koinViewModel()
) {
    val s by viewModel.state.collectAsState()
    LazyColumn(
        Modifier.fillMaxSize().background(DarkBg).padding(padding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Расходы", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(Icons.Outlined.FilterList, null, tint = TextWhite)
            }
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(CardBg).padding(4.dp)) {
                listOf("Неделя", "Месяц", "Год", "Всё время").forEachIndexed { i, t ->
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                        .background(if (s.period == i) OrangePrimary.copy(alpha = 0.3f) else Color.Transparent)
                        .clickable { viewModel.setPeriod(i) }.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center) {
                        Text(t, color = if (s.period == i) OrangePrimary else TextGray, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            if (s.period < 3) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = TextGray,
                        modifier = Modifier.clickable { viewModel.shift(-1) })
                    Text(s.title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray,
                        modifier = Modifier.clickable { viewModel.shift(1) })
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Donut(
                    s.slices,
                    modifier = Modifier.size(170.dp).padding(12.dp),
                    center = fmt(s.total)
                )
                Column(Modifier.weight(1f).padding(start = 16.dp)) {
                    s.slices.forEach { sl ->
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).background(sl.color, RoundedCornerShape(4.dp)))
                            Spacer(Modifier.width(8.dp))
                            Text(sl.label, color = TextWhite, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text("${sl.percent}%", color = TextGray, fontSize = 12.sp,
                                modifier = Modifier.width(44.dp), textAlign = TextAlign.Center)
                            Text("${fmt(sl.value)} ₽", color = TextWhite, fontSize = 12.sp,
                                modifier = Modifier.width(72.dp), textAlign = TextAlign.End)
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
        item {
            HorizontalDivider(color = Color(0xFF232733), thickness = 1.dp)
        }
        item { Text("История расходов", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                s.history.forEach { h ->
                    Card(colors = CardDefaults.cardColors(CardBg), shape = RoundedCornerShape(12.dp)) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(h.date, color = TextGray, fontSize = 12.sp)
                            Spacer(Modifier.width(16.dp))
                            Text(h.title, color = TextWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Text("${fmt(h.amount)} ₽", color = OrangePrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class Slice(val label: String, val value: Double, val percent: Int, val color: Color)

@Composable
private fun Donut(slices: List<Slice>, modifier: Modifier, center: String) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val total = slices.sumOf { it.value }.takeIf { it > 0 } ?: 1.0
            if (slices.isEmpty()) {
                drawCircle(ProgressTrack, style = Stroke(size.minDimension * 0.22f))
                return@Canvas
            }
            var start = -90f
            slices.forEachIndexed { i, sl ->
                val isLast = i == slices.lastIndex
                val sweep = if (isLast) 360f - (start + 90f) else (360f * sl.value / total).toFloat()
                val gap = if (slices.size > 1) 2f else 0f
                drawArc(sl.color, start, (sweep - gap).coerceAtLeast(0f), false,
                    style = Stroke(size.minDimension * 0.22f, cap = StrokeCap.Butt))
                start += sweep
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(center, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("₽", color = TextGray, fontSize = 20.sp)
        }
    }
}

private fun fmt(v: Double) =
    NumberFormat.getNumberInstance(Locale("ru", "RU")).apply { maximumFractionDigits = 0 }.format(v)