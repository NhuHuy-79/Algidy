package com.nhuhuy.algidy.feature.settings.utils

import androidx.annotation.StringRes
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.R

@StringRes
fun DarkMode.toStringRes(): Int {
    return when (this) {
        DarkMode.DARK -> R.string.setting_dark_mode
        DarkMode.SYSTEM -> R.string.setting_system_mode
        DarkMode.LIGHT -> R.string.setting_light_mode
    }
}

