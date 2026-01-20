package com.pusu.indexed.core.theme

data class ThemePreset(
    val id: String,
    val displayName: String,
    val primaryColor: Long,
    val secondaryColor: Long,
    val tertiaryColor: Long
)

fun defaultThemePresets(): List<ThemePreset> {
    return listOf(
        ThemePreset(
            id = "ocean",
            displayName = "海蓝",
            primaryColor = 0xFF1E88E5,
            secondaryColor = 0xFF1565C0,
            tertiaryColor = 0xFF64B5F6
        ),
        ThemePreset(
            id = "forest",
            displayName = "森林绿",
            primaryColor = 0xFF2E7D32,
            secondaryColor = 0xFF1B5E20,
            tertiaryColor = 0xFF81C784
        ),
        ThemePreset(
            id = "violet",
            displayName = "薰衣草",
            primaryColor = 0xFF7B1FA2,
            secondaryColor = 0xFF4A148C,
            tertiaryColor = 0xFFBA68C8
        ),
        ThemePreset(
            id = "sunset",
            displayName = "日落橙",
            primaryColor = 0xFFF57C00,
            secondaryColor = 0xFFEF6C00,
            tertiaryColor = 0xFFFFB74D
        ),
        ThemePreset(
            id = "rose",
            displayName = "玫瑰红",
            primaryColor = 0xFFD32F2F,
            secondaryColor = 0xFFC62828,
            tertiaryColor = 0xFFEF9A9A
        )
    )
}
