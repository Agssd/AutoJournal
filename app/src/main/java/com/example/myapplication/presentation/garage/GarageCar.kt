package com.example.myapplication.presentation.cars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.*
import com.example.myapplication.presentation.garage.components.GarageCarCard
import com.example.myapplication.presentation.garage.GarageCarViewModel
import com.example.myapplication.presentation.garage.components.GarageTopBar
import org.koin.androidx.compose.koinViewModel

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

    var toDelete by remember { mutableStateOf<com.example.myapplication.domain.model.Car?>(null) }

    toDelete?.let { car ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Удалить авто?") },
            text = { Text("«${car.brand} ${car.plate}» будет удалён без возможности восстановления.") },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(car.id); toDelete = null }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Отмена") }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(padding),
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
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(DarkBg).padding(inner),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cars, key = { it.id }) { car ->
                    GarageCarCard(
                        car = car,
                        onClick = { onCarClick(car.id) },
                        onEdit = { onCarEdit(car.id) },
                        onDelete = { toDelete = car }
                    )
                }
            }
        }
    }
}

