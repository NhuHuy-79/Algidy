package com.nhuhuy.algidy.feature.settings.utils

import androidx.annotation.StringRes
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.ThemeMode
import com.nhuhuy.algidy.core.presentation.R

@StringRes
fun ThemeMode.toStringRes(): Int {
    return when (this) {
        ThemeMode.DARK -> R.string.setting_dark_mode
        ThemeMode.SYSTEM -> R.string.setting_system_mode
        ThemeMode.LIGHT -> R.string.setting_light_mode
    }
}

@StringRes
fun AppLanguage.toStringRes(): Int {
    return when (this) {
        AppLanguage.ENGLISH -> R.string.setting_lang_english
        AppLanguage.VIETNAMESE -> R.string.setting_lang_vietnamese
        /*AppLanguage.SPANISH -> R.string.setting_lang_spanish
        AppLanguage.FRENCH -> R.string.setting_lang_french
        AppLanguage.GERMAN -> R.string.setting_lang_german
        AppLanguage.PORTUGUESE -> R.string.setting_lang_portuguese
        AppLanguage.INDONESIAN -> R.string.setting_lang_indonesian
        AppLanguage.ITALIAN -> R.string.setting_lang_italian*/
    }
}

