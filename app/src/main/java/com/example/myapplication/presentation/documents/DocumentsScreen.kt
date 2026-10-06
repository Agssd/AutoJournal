package com.example.myapplication.presentation.documents

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.core.theme.*
import org.koin.androidx.compose.koinViewModel
import java.io.File
import androidx.core.content.FileProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    padding: PaddingValues,
    onBack: () -> Unit,
    onOpen: (String) -> Unit = {},
    vm: DocumentsViewModel = koinViewModel()
) {
    val docs by vm.docs.collectAsState()
    val ctx = LocalContext.current
    var showType by remember { mutableStateOf(false) }
    var showSource by remember { mutableStateOf(false) }
    var pendingType by remember { mutableStateOf("STS") }
    var tmpUri by remember { mutableStateOf<Uri?>(null) }

    fun photoUri(): Uri {
        val f = File(ctx.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", f).also { tmpUri = it }
    }

    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { vm.add(ctx, pendingType, it, "image/jpeg") }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) tmpUri?.let { vm.add(ctx, pendingType, it, "image/jpeg") }
    }
    val file = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { vm.add(ctx, pendingType, it, ctx.contentResolver.getType(it) ?: "application/pdf") }
    }

    LazyColumn(
        Modifier.fillMaxSize().background(DarkBg).padding(padding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextWhite,
                    modifier = Modifier.clickable(onClick = onBack).padding(4.dp))
                Text("Документы", color = TextWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Box(Modifier.size(28.dp).clip(CircleShape).background(OrangePrimary)
                    .clickable { showType = true }, contentAlignment = Alignment.Center) {
                    Text("+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        if (docs.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    Text("Нет документов", color = TextGray, fontSize = 15.sp)
                }
            }
        }
        items(docs, key = { it.id }) { d ->
            Card(onClick = { onOpen(d.id) }, shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(CardBg)) {
                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(d.iconBg),
                        contentAlignment = Alignment.Center) {
                        Icon(d.icon, null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(d.title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text(d.subtitle, color = TextGray, fontSize = 12.sp)
                    }
                    if (d.isImage && d.previewUri.isNotBlank()) {
                        AsyncImage(model = d.previewUri, contentDescription = null,
                            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop)
                        Spacer(Modifier.width(8.dp))
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextGray)
                }
            }
        }
    }

    if (showType) ModalBottomSheet(onDismissRequest = { showType = false }) {
        listOf("STS" to "СТС", "PTS" to "ПТС", "OSAGO" to "Полис ОСАГО",
            "KASKO" to "Каско", "DIAG" to "Диагностическая карта", "OTHER" to "Другое").forEach { (id, label) ->
            ListItem(headlineContent = { Text(label) }, modifier = Modifier.clickable {
                pendingType = id; showType = false; showSource = true
            })
        }
    }
    if (showSource) ModalBottomSheet(onDismissRequest = { showSource = false }) {
        ListItem(headlineContent = { Text("Галерея") }, modifier = Modifier.clickable {
            showSource = false
            gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        })
        ListItem(headlineContent = { Text("Камера") }, modifier = Modifier.clickable {
            showSource = false; camera.launch(photoUri())
        })
        ListItem(headlineContent = { Text("Файл PDF") }, modifier = Modifier.clickable {
            showSource = false; file.launch(arrayOf("application/pdf", "image/*"))
        })
    }
}
// nav: composable("docs") { DocumentsScreen(padding, { navController.popBackStack() }) }
// ExpensesCard: onMenuClick("docs") -> navController.navigate("docs")