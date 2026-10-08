package com.example.myapplication.presentation.garage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.core.theme.*
import com.example.myapplication.domain.model.Car

@Composable
fun CarImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF222630)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Outlined.DirectionsCar, null, tint = TextGray, modifier = Modifier.size(64.dp))
    }
}

@Composable
fun GarageCarCard(
        car: Car,
    onClick: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onMore: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color(0xFF1A2027))
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(Modifier.fillMaxWidth()) {
                CarImagePlaceholder(Modifier.fillMaxWidth().height(170.dp))
                Row(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalIconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(18.dp))
                    }
                    FilledTonalIconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Outlined.Delete, null, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Spec("Марка автомобилей", car.brand)
            Spec("Госномер", car.plate)
            Spec("VIN", car.vin)
            Spec("Год выпуска", car.year.toString())
            Spec("Пробег", "%,d км".format(car.mileage).replace(',', ' '))
            Spec("Цвет", car.color.ifEmpty { "—" }, last = true)
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                FilledTonalButton(
                    onClick = onMore,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF232B34), contentColor = TextGray
                    )
                ) { Text("Подробнее ⌄", fontSize = 13.sp) }
            }
        }
    }
}

@Composable
private fun Spec(l: String, v: String, last: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        Arrangement.SpaceBetween, Alignment.CenterVertically
    ) {
        Text(l, color = TextGray, fontSize = 13.sp)
        Text(v.ifEmpty { "—" }, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
    if (!last) HorizontalDivider(color = ProgressTrack.copy(alpha = 0.3f), thickness = 0.5.dp)
}