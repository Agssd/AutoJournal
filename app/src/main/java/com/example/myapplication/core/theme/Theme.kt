package com.example.myapplication.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(

    primary = Primary,

    background = Background,

    surface = Surface,

    onPrimary = TextPrimary,

    onBackground = TextPrimary,

    onSurface = TextPrimary
)

@Composable
fun AutoJournalTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme = DarkColors,

        typography = AppTypography,

        content = content
    )

}