package com.example.myapplication.presentation.records

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.OilBarrel
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.myapplication.core.theme.DarkBg
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite
import com.example.myapplication.presentation.record.components.CostBox
import com.example.myapplication.presentation.record.components.CountBox
import com.example.myapplication.presentation.record.components.DateBox
import com.example.myapplication.presentation.record.components.DescField
import com.example.myapplication.presentation.record.components.MileageBox
import com.example.myapplication.presentation.record.components.RowLine
import com.example.myapplication.presentation.record.components.TitleField
import org.koin.androidx.compose.koinViewModel
import java.io.File

private data class RecType(val id: String, val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    padding: PaddingValues,
    onBack: () -> Unit,
    onAddCar: () -> Unit = {},
    viewModel: RecordScreenViewModel = koinViewModel()
) {
    val s by viewModel.state.collectAsState()
    val cars by viewModel.cars.collectAsState()
    val carId by viewModel.selectedCarId.collectAsState()

    val types = listOf(
        RecType("oil", "ТО / Масло", Icons.Outlined.OilBarrel),
        RecType("repair", "Ремонт", Icons.Outlined.Build),
        RecType("parts", "Расходники", Icons.Outlined.ShoppingBag),
        RecType("tires", "Шины", Icons.Outlined.Album),
        RecType("fuel", "Заправка", Icons.Outlined.LocalGasStation),
        RecType("other", "Другие", Icons.Outlined.MoreHoriz),
    )

    val ctx = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { viewModel.addPhoto(it) }
        }
    var tmpUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            if (ok) tmpUri?.let { viewModel.addPhoto(it) }
        }

    fun photoUri(): Uri {
        val f = File(ctx.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", f)
            .also { tmpUri = it }
    }

    Column(
        Modifier.fillMaxSize().background(DarkBg)
            .padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextWhite,
                modifier = Modifier.clickable(onClick = onBack).padding(4.dp)
            )
            Text(
                "Новая запись", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )
            Box(
                Modifier.size(28.dp).clip(CircleShape).background(OrangePrimary)
                    .clickable(enabled = cars.isNotEmpty()) { viewModel.save(onBack) },
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(16.dp))

        if (cars.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(Color(0xFF1E242C)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 32.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.DirectionsCar, null,
                            tint = TextGray, modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Нет авто",
                            color = TextWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Запись недоступна — сначала добавьте автомобиль",
                            color = TextGray, fontSize = 14.sp, textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = onAddCar,
                            colors = ButtonDefaults.buttonColors(OrangePrimary),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(52.dp).fillMaxWidth(0.7f)
                        ) {
                            Text("Добавить авто", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            return@Column
        }
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded, { expanded = it }) {
            OutlinedTextField(
                value = cars.find { it.id.toString() == carId?.toString() }
                    ?.let { "${it.brand} ${it.plate}" } ?: "Выберите авто",
                onValueChange = {}, readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )
            ExposedDropdownMenu(expanded, { expanded = false }) {
                cars.forEach { c ->
                    DropdownMenuItem(
                        text = { Text("${c.brand} ${c.plate}") },
                        onClick = {
                            viewModel.selectCar(
                                c.id.toString().toLongOrNull() ?: 0
                            ); expanded = false
                        }
                    )
                }
            }
        }
        if (carId == null) {
            Text(
                "Выберите автомобиль",
                color = OrangePrimary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(12.dp))
        Text("Тип записи", color = TextGray, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))

        types.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { t ->
                    val sel = s.type == t.id
                    Column(
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A2027))
                            .border(
                                if (sel) 1.5.dp else 0.dp,
                                if (sel) OrangePrimary else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.setType(t.id) }.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            t.icon,
                            null,
                            tint = if (sel) OrangePrimary else TextWhite,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            t.label,
                            color = if (sel) OrangePrimary else TextWhite,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        when (s.type) {
            "oil" -> {
                RowLine("Дата") { DateBox(s.date, viewModel::setDate) }
                RowLine("Пробег") { MileageBox(s.mileage, viewModel::setMileage) }
                TitleField("Название", s.title, viewModel::setTitle, "Замена масла")
                DescField(s.desc, viewModel::setDesc)
                RowLine("Стоимость") { CostBox(s.cost, viewModel::setCost) }
            }

            "repair" -> {
                RowLine("Дата") { DateBox(s.date, viewModel::setDate) }
                RowLine("Пробег") { MileageBox(s.mileage, viewModel::setMileage) }
                TitleField("Что ремонтировали", s.title, viewModel::setTitle, "Замена колодок")
                TitleField("Сервис", s.extra1, viewModel::setExtra1, "СТО")
                RowLine("Стоимость") { CostBox(s.cost, viewModel::setCost) }
            }

            "parts" -> {
                RowLine("Дата") { DateBox(s.date, viewModel::setDate) }
                TitleField("Название", s.title, viewModel::setTitle, "Фильтр Mann")
                RowLine("Количество") { CountBox(s.extra1, viewModel::setExtra1) }
                RowLine("Стоимость") { CostBox(s.cost, viewModel::setCost) }
            }

            "tires" -> {
                RowLine("Дата") { DateBox(s.date, viewModel::setDate) }
                RowLine("Пробег") { MileageBox(s.mileage, viewModel::setMileage) }
                TitleField("Размер", s.extra1, viewModel::setExtra1, "205/55 R16")
                TitleField("Сезон", s.extra2, viewModel::setExtra2, "Зима / Лето")
                RowLine("Стоимость") { CostBox(s.cost, viewModel::setCost) }
            }

            "fuel" -> {
                RowLine("Дата") { DateBox(s.date, viewModel::setDate) }
                RowLine("Пробег") { MileageBox(s.mileage, viewModel::setMileage) }
                RowLine("Итого *") { CostBox(s.cost, viewModel::setCost) }
                TitleField("АЗС", s.title, viewModel::setTitle, "Лукойл")
                RowLine("Литры") { CountBox(s.extra1, viewModel::setExtra1) }
                RowLine("Цена / л") { CostBox(s.extra2, viewModel::setExtra2) }
            }

            else -> {
                RowLine("Дата") { DateBox(s.date, viewModel::setDate) }
                TitleField("Название", s.title, viewModel::setTitle, "Мойка")
                DescField(s.desc, viewModel::setDesc)
                RowLine("Стоимость") { CostBox(s.cost, viewModel::setCost) }
            }
        }

        var showSheet by remember { mutableStateOf(false) }
        val photos by viewModel.photos.collectAsState()

        Spacer(Modifier.height(8.dp))
        Text("Фотографии", color = TextWhite, fontSize = 14.sp)
        Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            photos.forEach { uri ->
                AsyncImage(
                    model = uri, contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
                .border(1.dp, TextGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .clickable { showSheet = true }, contentAlignment = Alignment.Center
            ) {
                Text("+", color = TextGray, fontSize = 28.sp)
            }
        }

        if (showSheet) ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            ListItem(headlineContent = { Text("Камера") }, modifier = Modifier.clickable {
                showSheet = false; cameraLauncher.launch(photoUri())
            })
            ListItem(headlineContent = { Text("Галерея") }, modifier = Modifier.clickable {
                showSheet = false
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            })
        }
        s.error?.let {
            Text(
                it,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}