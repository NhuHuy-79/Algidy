package com.nhuhuy.algidy.widget.utils

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.glance.material3.ColorProviders
import androidx.glance.unit.ColorProvider
import com.nhuhuy.algidy.core.designsystem.theme.onPrimaryContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.onPrimaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.onSecondaryDark
import com.nhuhuy.algidy.core.designsystem.theme.onSecondaryLight
import com.nhuhuy.algidy.core.designsystem.theme.onSurfaceDark
import com.nhuhuy.algidy.core.designsystem.theme.onSurfaceLight
import com.nhuhuy.algidy.core.designsystem.theme.primaryContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.primaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.primaryDark
import com.nhuhuy.algidy.core.designsystem.theme.primaryLight
import com.nhuhuy.algidy.core.designsystem.theme.secondaryContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.secondaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.secondaryDark
import com.nhuhuy.algidy.core.designsystem.theme.secondaryLight
import com.nhuhuy.algidy.core.designsystem.theme.surfaceDark
import com.nhuhuy.algidy.core.designsystem.theme.surfaceLight

object WidgetColorScheme {
    val colors = ColorProviders(
        light = lightColorScheme(
            primary = primaryLight, // Lấy từ Color.kt của bạn
            surface = surfaceLight,
            onSurface = onSurfaceLight,
            secondaryContainer = secondaryContainerLight,
            secondary = secondaryLight,
            onSecondary = onSecondaryLight,
            primaryContainer = primaryContainerLight,
            onPrimaryContainer = onPrimaryContainerLight
        ),
        dark = darkColorScheme(
            primary = primaryDark,
            surface = surfaceDark,
            onSurface = onSurfaceDark,
            secondaryContainer = secondaryContainerDark,
            secondary = secondaryDark,
            onSecondary = onSecondaryDark,
            primaryContainer = primaryContainerDark,
            onPrimaryContainer = onPrimaryContainerDark
        )
    )
}

enum class WidgetColors {
    BACKGROUND, ON_BACKGROUND,
    LIST_ITEM_BACKGROUND, LIST_ITEM_CONTENT,
}

@Composable
fun WidgetColors.toColorProvider(): ColorProvider {
    WidgetColorScheme.colors.run {
        return when (this@toColorProvider) {
            WidgetColors.BACKGROUND -> secondary
            WidgetColors.ON_BACKGROUND -> onSecondary
            WidgetColors.LIST_ITEM_BACKGROUND -> secondaryContainer
            WidgetColors.LIST_ITEM_CONTENT -> secondary

        }
    }
}