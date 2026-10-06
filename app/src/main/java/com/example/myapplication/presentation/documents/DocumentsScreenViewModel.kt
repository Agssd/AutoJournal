package com.example.myapplication.presentation.documents

import android.content.Context
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.dao.DocumentDao
import com.example.myapplication.data.entity.DocumentEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

data class DocUi(
    val id: String, val title: String, val subtitle: String,
    val icon: ImageVector, val iconBg: Color,
    val previewUri: String = "", val isImage: Boolean = false
)

class DocumentsViewModel(private val dao: DocumentDao) : ViewModel() {
    private val icons = mapOf(
        "STS" to (Icons.Outlined.Badge to Color(0xFF2E7D32)),
        "PTS" to (Icons.Outlined.Badge to Color(0xFF1565C0)),
        "OSAGO" to (Icons.Outlined.Shield to Color(0xFF2E7D32)),
        "KASKO" to (Icons.Outlined.Shield to Color(0xFF6A1B9A)),
        "DIAG" to (Icons.Outlined.Description to Color(0xFF1565C0)),
        "OTHER" to (Icons.Outlined.PictureAsPdf to Color(0xFFC62828))
    )
    private val titles = mapOf(
        "STS" to "СТС", "PTS" to "ПТС", "OSAGO" to "Полис ОСАГО",
        "KASKO" to "Каско", "DIAG" to "Диагностическая карта", "OTHER" to "Документ")

    val docs: StateFlow<List<DocUi>> = dao.observe().map { list ->
        list.map { e ->
            val (icon, bg) = icons[e.type] ?: icons.getValue("OTHER")
            DocUi(
                id = e.id.toString(),
                title = e.title.ifEmpty { titles[e.type] ?: e.type },
                subtitle = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(e.createdAt)),
                icon = icon, iconBg = bg,
                previewUri = e.fileUri, isImage = e.mime.startsWith("image")
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun add(ctx: Context, type: String, src: Uri, mime: String) = viewModelScope.launch {
        val ext = if (mime.contains("pdf")) "pdf" else "jpg"
        val dst = File(ctx.filesDir, "documents/doc_${System.currentTimeMillis()}.$ext")
        dst.parentFile?.mkdirs()
        ctx.contentResolver.openInputStream(src)?.use { ins ->
            FileOutputStream(dst).use { ins.copyTo(it) }
        } ?: return@launch
        dao.insert(
            DocumentEntity(
                type = type, title = titles[type] ?: type,
                fileUri = dst.absolutePath, mime = mime.ifEmpty { "image/jpeg" })
        )
    }

    fun delete(id: String) = viewModelScope.launch {
    }
}