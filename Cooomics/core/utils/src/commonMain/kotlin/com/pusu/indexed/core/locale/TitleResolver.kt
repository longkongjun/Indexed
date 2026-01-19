package com.pusu.indexed.core.locale

fun resolveTitle(
    defaultTitle: String,
    titleEnglish: String?,
    titleJapanese: String?,
    language: AppLanguage
): String {
    return when (language) {
        AppLanguage.English -> titleEnglish?.takeIf { it.isNotBlank() } ?: defaultTitle
        AppLanguage.Chinese -> titleJapanese?.takeIf { it.isNotBlank() } ?: defaultTitle
    }
}
