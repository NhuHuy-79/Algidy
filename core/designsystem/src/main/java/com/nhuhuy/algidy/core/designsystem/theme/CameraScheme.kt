package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CameraColorScheme(
    val background: Color = surfaceDark,
    val foreground: Color = onSurfaceDark,
    val primary: Color = primaryLightMediumContrast,
    val onPrimary: Color = onPrimaryLightMediumContrast,
    val secondaryContainer: Color = secondaryLight,
    val onSecondaryContainer: Color = onSecondaryLight
)

val LocalCameraColorScheme = staticCompositionLocalOf { CameraColorScheme() }