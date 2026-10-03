package com.example.myapplication.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.core.theme.CardBg
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite

@Composable
fun EmptyGarageCard(onAddClick: () -> Unit) {
    Card(
        onClick = onAddClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.AddCircleOutline, null, tint = TextGray, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text("Добавить транспортное средство", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text("Нажмите, чтобы добавить авто", color = TextGray, fontSize = 13.sp)
        }
    }
}