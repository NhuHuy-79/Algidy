package com.nhuhuy.algidy.core.presentation.utils

import androidx.compose.ui.graphics.Color
import com.nhuhuy.algidy.core.model.setting.SeedColor

fun SeedColor.toColor(): Color {
    return when (this) {
        SeedColor.SAPPHIRE -> Color(0xFF3F7FC4)
        SeedColor.RUBY -> Color(0xFFC95656)
        SeedColor.TOPAZ -> Color(0xFFD99A32)
        SeedColor.EMERALD -> Color(0xFF4F8A52)
        SeedColor.AMETHYST -> Color(0xFF7B6AA8)
    }
}