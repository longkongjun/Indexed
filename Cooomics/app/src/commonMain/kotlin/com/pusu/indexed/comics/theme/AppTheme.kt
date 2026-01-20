package com.pusu.indexed.comics.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pusu.indexed.core.theme.ThemePreset

@Composable
fun AppTheme(
    preset: ThemePreset,
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = Color(preset.primaryColor),
        secondary = Color(preset.secondaryColor),
        tertiary = Color(preset.tertiaryColor)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
