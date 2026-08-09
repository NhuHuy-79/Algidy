package com.nhuhuy.algidy.core.designsystem.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Material You Expressive Shape Tokens.
 * Expressive design uses higher corner radii to create a more friendly and modern look.
 */
@Immutable
data class AlgidyShapes(
    val none: Shape = RoundedCornerShape(0.dp),
    val extraSmall: Shape = RoundedCornerShape(4.dp),
    val small: Shape = RoundedCornerShape(8.dp),
    val medium: Shape = RoundedCornerShape(12.dp),
    val large: Shape = RoundedCornerShape(16.dp),
    val extraLarge: Shape = RoundedCornerShape(28.dp),      // Expressive Card/Sheet standard
    val extraExtraLarge: Shape = RoundedCornerShape(32.dp), // Modal/Dialog standard
    val full: Shape = RoundedCornerShape(100)               // Capsule/Button standard
)

val LocalAlgidyShapes = staticCompositionLocalOf { AlgidyShapes() }
