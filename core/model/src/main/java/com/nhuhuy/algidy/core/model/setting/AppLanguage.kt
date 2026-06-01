package com.nhuhuy.algidy.core.model.setting

enum class AppLanguage(val isoCode: String) {
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    GERMAN("de"),
    PORTUGUESE("pt"),
    INDONESIAN("id"),
    ITALIAN("it"),
    VIETNAMESE("vi");

    companion object {
        fun fromIsoCode(code: String?): AppLanguage {
            return entries.find { it.isoCode == code } ?: ENGLISH
        }
    }
}

fun String?.toAppLanguage(): AppLanguage = AppLanguage.fromIsoCode(this)

fun AppLanguage.toDisplayName(): String {
    return when (this) {
        AppLanguage.VIETNAMESE -> "Tiếng Việt"
        AppLanguage.ENGLISH -> "English"
        AppLanguage.SPANISH -> "Español"
        AppLanguage.FRENCH -> "Français"
        AppLanguage.GERMAN -> "Deutsch"
        AppLanguage.PORTUGUESE -> "Português"
        AppLanguage.INDONESIAN -> "Bahasa Indonesia"
        AppLanguage.ITALIAN -> "Italiano"
    }
}