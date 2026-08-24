package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CameraColorScheme(
    val background: Color = surfaceDark,
    val foreground: Color = onSurfaceDark,
    val primaryContainer: Color = primaryContainerDark,
    val onPrimaryContainer: Color = onPrimaryContainerDark,
    val secondaryContainer: Color = secondaryDark,
    val onSecondaryContainer: Color = onSecondaryDark
)

val LocalCameraColorScheme = staticCompositionLocalOf { CameraColorScheme() }