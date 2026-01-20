package com.pusu.indexed.shared.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pusu.indexed.core.locale.AppLanguage
import com.pusu.indexed.core.theme.ThemePreset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    themeOptions: List<ThemePreset>,
    currentThemeId: String,
    onThemeChange: (ThemePreset) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "语言",
                style = MaterialTheme.typography.titleMedium
            )

            AppLanguage.values().forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLanguage == option,
                        onClick = {
                            onLanguageChange(option)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option.displayName)
                }
            }

            HorizontalDivider()

            Text(
                text = "主题色",
                style = MaterialTheme.typography.titleMedium
            )

            themeOptions.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentThemeId == option.id,
                        onClick = { onThemeChange(option) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ThemeColorDot(color = Color(option.primaryColor))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option.displayName)
                }
            }
        }
    }
}

@Composable
private fun ThemeColorDot(color: Color) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier.size(16.dp),
        onDraw = {
            drawCircle(color = color)
        }
    )
}
