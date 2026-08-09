package com.nhuhuy.algidy.core.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material You Expressive Spacing Tokens.
 * Following Google's 4dp/8dp grid system for consistent layouts.
 */
@Immutable
data class AlgidySpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,    // 4dp
    val small: Dp = 8.dp,         // 8dp
    val medium: Dp = 12.dp,       // 12dp
    val large: Dp = 16.dp,        // 16dp - Standard gutter
    val extraLarge: Dp = 24.dp,   // 24dp - Expressive spacing
    val extraExtraLarge: Dp = 32.dp, // 32dp
    val huge: Dp = 48.dp,         // 48dp
    val massive: Dp = 64.dp       // 64dp
)

val LocalAlgidySpacing = staticCompositionLocalOf { AlgidySpacing() }
