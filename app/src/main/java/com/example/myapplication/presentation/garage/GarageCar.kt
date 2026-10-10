package com.example.myapplication.presentation.cars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.CardBg
import com.example.myapplication.core.theme.DarkBg
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite
import com.example.myapplication.presentation.garage.GarageCarViewModel
import com.example.myapplication.presentation.garage.components.GarageCarCard
import com.example.myapplication.presentation.garage.components.GarageTopBar
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GarageCar(
    padding: PaddingValues,
    onBack: () -> Unit,
    onAddCar: () -> Unit,
    onCarClick: (String) -> Unit = {},
    onCarEdit: (String) -> Unit = {},
    viewModel: GarageCarViewModel = koinViewModel()
) {
    val cars by viewModel.cars.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    var toDelete by remember { mutableStateOf<com.example.myapplication.domain.model.Car?>(null) }

    toDelete?.let { car ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Удалить авто?") },
            text = { Text("«${car.brand} ${car.plate}» будет удалён без возможности восстановления.") },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(car.id); toDelete = null }) {
                    Text("Удалить", color = androidx.compose.ui.graphics.Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Отмена") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(DarkBg).padding(padding),
        containerColor = DarkBg,
        topBar = {
            GarageTopBar(title = "Мои автомобили", onBack = onBack, isListMode = true, onAdd = onAddCar)
        }
    ) { inner ->
        if (cars.isEmpty()) {
            Box(Modifier.fillMaxSize().background(DarkBg).padding(inner), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Нет авто", color = TextWhite, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onAddCar, colors = ButtonDefaults.buttonColors(OrangePrimary)) {
                        Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); Text("Добавить")
                    }
                }
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { cars.size })
            val currentCar = cars.getOrNull(pagerState.currentPage)
            val history = remember(expenses, currentCar) {
                val cid = currentCar?.id?.toLongOrNull() ?: -1L
                expenses.filter { it.carId == cid }
                    .sortedByDescending { it.timestamp }
                    .groupBy {
                        SimpleDateFormat("LLLL yyyy", Locale("ru")).format(Date(it.timestamp))
                            .replaceFirstChar { c -> c.uppercase() }
                    }
            }
            Column(Modifier.fillMaxSize().background(DarkBg).padding(inner)) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.height(480.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    pageSpacing = 12.dp
                ) { page ->
                    val car = cars[page]
                    GarageCarCard(
                        car = car,
                        onClick = { onCarClick(car.id) },
                        onEdit = { onCarEdit(car.id) },
                        onDelete = { toDelete = car }
                    )
                }
                if (cars.size > 1) {
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), Arrangement.Center) {
                        repeat(cars.size) { i ->
                            Box(Modifier.padding(4.dp)
                                .size(if (i == pagerState.currentPage) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (i == pagerState.currentPage) OrangePrimary else TextGray.copy(alpha = 0.4f)))
                        }
                    }
                }
                LazyColumn(
                    Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    history.forEach { (month, items) ->
                        item {
                            Text(month, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp))
                        }
                        items(items, key = { it.id }) { e ->
                            Card(colors = CardDefaults.cardColors(CardBg), shape = RoundedCornerShape(12.dp)) {
                                Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)  {
                                        Text(
                                            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(e.timestamp)),
                                            color = TextGray, fontSize = 14.sp,
                                            modifier = Modifier.width(76.dp)
                                        )
                                        Spacer(Modifier.width(24.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(e.title.ifEmpty { "Запись" }, color = TextWhite, fontSize = 13.sp)
                                            if (e.mileage > 0) Text(
                                                "%,d км".format(e.mileage).replace(',', ' '),
                                                color = TextGray, fontSize = 14.sp
                                            )
                                        }
                                        Spacer(Modifier.width(24.dp))
                                        Text(
                                            "${NumberFormat.getNumberInstance(Locale("ru", "RU")).apply { maximumFractionDigits = 0 }.format(e.amount)} ₽",
                                            color = OrangePrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (history.isEmpty()) item {
                        Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Нет расходов", color = TextGray, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}