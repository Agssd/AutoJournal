package com.example.myapplication.presentation.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite

@Composable
fun RowLine(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextGray, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Row(content = content)
    }
}

@Composable
fun DarkBox(m: Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        m
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1A2027))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically, content = content
    )
}

@Composable
fun RowScope.FieldText(v: String, on: (String) -> Unit, hint: String) {
    androidx.compose.foundation.text.BasicTextField(
        value = v, onValueChange = on, singleLine = true,
        textStyle = TextStyle(color = TextWhite, fontSize = 14.sp),
        modifier = Modifier.weight(1f),
        decorationBox = { inner ->
            if (v.isEmpty() && hint.isNotEmpty()) Text(hint, color = TextGray, fontSize = 14.sp)
            inner()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.DateBox(v: String, on: (String) -> Unit) {
    var show by remember { mutableStateOf(false) }
    DarkBox(Modifier
        .width(150.dp)
        .clickable { show = true }) {
        Text(v, color = TextWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.CalendarMonth, null, tint = TextGray, modifier = Modifier.size(16.dp))
    }
    if (show) {
        val picker = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    utcTimeMillis <= System.currentTimeMillis()
            }
        )
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    picker.selectedDateMillis?.let {
                        on(
                            java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
                                .format(java.util.Date(it))
                        )
                    }
                    show = false
                }) { Text("ОК") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Отмена") } }
        ) { DatePicker(state = picker) }
    }
}

@Composable
fun RowScope.MileageBox(v: String, on: (String) -> Unit) = DarkBox(Modifier.width(150.dp)) {
    FieldText(v, on, "245 300")
    Spacer(Modifier.width(6.dp)); Text("км", color = TextGray, fontSize = 12.sp)
}

@Composable
fun RowScope.CostBox(v: String, on: (String) -> Unit) = DarkBox(Modifier.width(100.dp)) {
    FieldText(v, on, "128")
}

@Composable
fun RowScope.CountBox(v: String, on: (String) -> Unit) = DarkBox(Modifier.width(100.dp)) {
    FieldText(v, on, "1")
}

@Composable
fun TitleField(label: String, v: String, on: (String) -> Unit, hint: String) {
    Text(label, color = TextGray, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
    DarkBox(Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)) { FieldText(v, on, hint) }
}

@Composable
fun DescField(v: String, on: (String) -> Unit) {
    Text(
        "Описание (необязательно)",
        color = TextGray,
        fontSize = 13.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
    DarkBox(Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)) { FieldText(v, on, "Liqui Moly 5W-30") }
}