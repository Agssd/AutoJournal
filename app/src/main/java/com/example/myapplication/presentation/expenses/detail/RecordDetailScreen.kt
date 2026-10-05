package com.example.myapplication.presentation.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.core.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordDetailScreen(
    recordId: String,
    padding: PaddingValues,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    vm: RecordDetailViewModel = koinViewModel()
) {
    val s by vm.state.collectAsState()
    LaunchedEffect(recordId) { vm.load(recordId) }

    Column(Modifier.fillMaxSize().background(DarkBg).padding(padding).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextWhite) }
            Text("Детали записи", color = TextWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f))
            IconButton(onClick = { onEdit(recordId) }) { Icon(Icons.Outlined.Edit, null, tint = TextWhite) }
        }
        Spacer(Modifier.height(16.dp))
        if (s.loading) { CircularProgressIndicator(color = OrangePrimary); return@Column }
        s.expense ?: run { Text("Запись не найдена", color = TextGray); return@Column }

        DetailRow("Тип", s.typeLabel)
        DetailRow("Автомобиль", s.carLabel)
        DetailRow("Дата", s.date)
        DetailRow("Пробег", s.mileage.ifEmpty { "—" })
        DetailRow("Название", s.expense!!.title.ifEmpty { "—" })
        if (s.expense!!.description.isNotBlank()) DetailRow("Описание", s.expense!!.description)
        DetailRow("Стоимость", "${s.cost} ₽", highlight = true)
        Text("Фотографии", color = TextGray, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp, bottom = 6.dp))
        if (s.photos.isEmpty()) {
            Text("Нет фото", color = TextGray, fontSize = 13.sp)
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                s.photos.forEach { uri ->
                    coil.compose.AsyncImage(
                        model = uri, contentDescription = null,
                        modifier = Modifier.size(160.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { onEdit(recordId) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(OrangePrimary)
        ) { Text("Редактировать", fontSize = 16.sp) }
    }
}

@Composable
private fun DetailRow(label: String, value: String, highlight: Boolean = false) {
    Card(colors = CardDefaults.cardColors(Color(0xFF1A2027)), shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(label, color = TextGray, fontSize = 13.sp)
            Text(value, color = if (highlight) OrangePrimary else TextWhite,
                fontSize = 14.sp, fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal)
        }
    }
}