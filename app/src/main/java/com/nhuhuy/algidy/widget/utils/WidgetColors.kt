package com.nhuhuy.algidy.widget.utils

import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.unit.ColorProvider

enum class WidgetColors {
    BACKGROUND, ON_BACKGROUND,
    LIST_ITEM_BACKGROUND, LIST_ITEM_CONTENT,
}

@Composable
fun WidgetColors.toColorProvider(): ColorProvider {
    return when (this) {
        WidgetColors.BACKGROUND -> GlanceTheme.colors.surface
        WidgetColors.ON_BACKGROUND -> GlanceTheme.colors.onSurface
        WidgetColors.LIST_ITEM_BACKGROUND -> GlanceTheme.colors.surfaceVariant
        WidgetColors.LIST_ITEM_CONTENT -> GlanceTheme.colors.onSurfaceVariant
    }
}