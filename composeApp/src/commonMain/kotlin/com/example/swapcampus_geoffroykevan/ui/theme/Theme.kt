package com.example.swapcampus_geoffroykevan.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
expect fun rememberSwapCampusColorScheme(darkTheme: Boolean): ColorScheme

@Composable
fun SwapCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = rememberSwapCampusColorScheme(darkTheme),
        typography = Typography,
        content = content,
    )
}
