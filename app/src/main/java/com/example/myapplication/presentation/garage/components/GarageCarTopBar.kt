package com.example.myapplication.presentation.garage.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.TextWhite

@Composable
fun GarageTopBar(
    title: String,
    onBack: () -> Unit,
    isListMode: Boolean = true,
    onAdd: () -> Unit = {},
    onEdit: () -> Unit = {},
    onMore: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextWhite)
        }
        Text(
            text = title,
            color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        if (isListMode) {
            IconButton(onClick = onAdd) {
                Icon(Icons.Filled.Add, null, tint = OrangePrimary)
            }
        } else {
            IconButton(onClick = onEdit) {
                Icon(Icons.Outlined.Edit, null, tint = TextWhite)
            }
            IconButton(onClick = onMore) {
                Icon(Icons.Filled.MoreVert, null, tint = TextWhite)
            }
        }
    }
}