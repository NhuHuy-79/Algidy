package com.nhuhuy.algidy.core.designsystem.motion

import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset

object AlgidyTransition {

    private const val DURATION = 400

    private val StandardSpec = tween<IntOffset>(
        durationMillis = DURATION,
    )

    private val EmphasizedSpec = tween<IntOffset>(
        durationMillis = DURATION,
        easing = EaseOutQuart,
    )

    val Forward = NavTransitionSpec(

        transitionSpec = {
            (
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = EmphasizedSpec,
                    ) + fadeIn()
                    ) togetherWith (
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = StandardSpec,
                    ) + fadeOut()
                    )
        },

        popTransitionSpec = {
            (
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = StandardSpec,
                    ) + fadeIn()
                    ) togetherWith (
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = EmphasizedSpec,
                    ) + fadeOut()
                    )
        },

        predictivePopTransitionSpec = {
            (
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                    ) +
                            fadeIn() +
                            scaleIn(initialScale = 0.9f)
                    ) togetherWith (
                    slideOutHorizontally(
                        targetOffsetX = { it },
                    ) + fadeOut()
                    )
        },
    )
}