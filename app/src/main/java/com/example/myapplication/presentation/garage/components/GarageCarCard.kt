package com.example.myapplication.presentation.garage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun GarageCarCard(car: Car, onClick: () -> Unit = {}) {
    Card(onClick = onClick, shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(CardBg)) {
        Column(Modifier.padding(12.dp)) {
            CarImagePlaceholder(Modifier.fillMaxWidth().height(140.dp))
            Spacer(Modifier.height(12.dp))
            Text(car.brand, color = TextWhite, fontSize = 16.sp)
            Text(car.plate, color = TextGray, fontSize = 13.sp)
            Text("${car.mileage} км", color = TextWhite, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { car.maintenanceProgress },
                modifier = Modifier.fillMaxWidth(),
                color = OrangePrimary, trackColor = ProgressTrack
            )
        }
    }
}