package com.nhuhuy.algidy.core.presentation.utils

import androidx.compose.ui.graphics.Color
import com.nhuhuy.algidy.core.model.setting.SeedColor

fun SeedColor.toColor(): Color {
    return when (this) {
        SeedColor.SAPPHIRE -> Color(0xFF1565C0)
        SeedColor.RUBY -> Color(0xFFC62828)
        SeedColor.TOPAZ -> Color(0xFFFF8F00)
        SeedColor.EMERALD -> Color(0xFF2E7D32)
        SeedColor.AMETHYST -> Color(0xFF6750A4)
    }
}