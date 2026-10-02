package com.example.myapplication.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.theme.CardBg
import com.example.myapplication.core.theme.OrangePrimary
import com.example.myapplication.core.theme.ProgressTrack
import com.example.myapplication.core.theme.TextGray
import com.example.myapplication.core.theme.TextWhite
import com.example.myapplication.domain.model.Car
import com.example.myapplication.ui.theme.*

@Composable
fun CarCard(
    car: Car,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Универсальный силуэт авто
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF222630)), // Темная подложка
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DirectionsCar, // Или painterResource(R.drawable.ic_car_silhouette)
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Текстовый блок (Бренд, номер, пробег и ТО)
            Column(modifier = Modifier.weight(0.6f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = car.brand,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        if (car.isFavorite) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(text = car.plate, fontSize = 13.sp, color = TextGray)
                Text(
                    text = "${car.mileage} км",
                    fontSize = 13.sp,
                    color = TextWhite,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Прогресс-бар
                LinearProgressIndicator(
                    progress = { car.maintenanceProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = OrangePrimary,
                    trackColor = ProgressTrack
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Текст ТО
                Text(
                    text = buildAnnotatedString {
                        append(car.maintenanceTitle)
                        withStyle(style = SpanStyle(color = OrangePrimary, fontWeight = FontWeight.Bold)) {
                            append(car.maintenanceSubtitle)
                        }
                    },
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }
    }
}