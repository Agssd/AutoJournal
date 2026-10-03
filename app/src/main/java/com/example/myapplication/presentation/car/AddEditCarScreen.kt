package com.example.myapplication.presentation.car

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.*
import com.example.myapplication.presentation.cars.AddEditCarViewModel
import com.example.myapplication.presentation.garage.components.GarageTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddEditCarScreen(
    carId: String? = null,
    padding: PaddingValues,
    onBack: () -> Unit,
    viewModel: AddEditCarViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(carId) { viewModel.init(carId) }

    Column(Modifier.fillMaxSize().background(DarkBg).padding(padding).padding(16.dp)) {
        GarageTopBar(
            title = if (carId == null) "Добавить авто" else "Редактировать",
            onBack = onBack, isListMode = false, onEdit = {}, onMore = {}
        )
        Spacer(Modifier.height(16.dp))
        Field("Марка / модель *", state.brand, viewModel::onBrand)
        Field("Госномер *", state.plate, viewModel::onPlate)
        Field("Пробег, км *", state.mileage, viewModel::onMileage, isNumber = true)
        Field("VIN", state.vin, viewModel::onVin)
        Field("Год выпуска", state.year, viewModel::onYear, isNumber = true)
        Field("Цвет", state.color, viewModel::onColor)
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { viewModel.save(onBack) },
            enabled = state.canSave && !state.saving,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(OrangePrimary)
        ) { Text(if (state.saving) "Сохранение..." else "Сохранить", fontSize = 16.sp) }
        state.error?.let { Text(it, color = androidx.compose.ui.graphics.Color.Red) }
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit, isNumber: Boolean = false) {
    Column(Modifier.padding(bottom = 12.dp)) {
        Text(label, color = TextGray, fontSize = 13.sp)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value, onValueChange = onChange, singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
        )
    }
}