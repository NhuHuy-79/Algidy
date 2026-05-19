package com.nhuhuy.algidy.core.model.setting

enum class AppLanguage(val isoCode: String) {
    VIETNAMESE("vi"),
    ENGLISH("en");

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
    }
}