package com.nhuhuy.algidy.core.designsystem.motion

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.ui.graphics.TransformOrigin

object AlgidyMotion {

    val verticalToolbar = VisibilityTransition(
        enter = fadeIn(
            animationSpec = tween(durationMillis = 200)
        ) + slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + scaleIn(
            transformOrigin = TransformOrigin(1f, 0.5f),
            initialScale = 0.8f,
            animationSpec = tween(durationMillis = 250)
        ),
        exit = shrinkOut() + fadeOut()
    )
}