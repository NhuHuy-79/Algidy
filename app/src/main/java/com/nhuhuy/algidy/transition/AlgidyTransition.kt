package com.nhuhuy.algidy.transition

import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object AppTransitions {

    val enterFromRight = NavigationTransition(
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(
                400,
                easing = EaseOutQuart,
            ),
        ) + fadeIn(),

        exit = slideOutHorizontally(
            targetOffsetX = { -it / 3 },
            animationSpec = tween(400),
        ) + fadeOut(),
    )

    val enterFromLeft = NavigationTransition(
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(
                400,
                easing = EaseOutQuart,
            ),
        ) + fadeIn(),

        exit = slideOutHorizontally(
            targetOffsetX = { it / 3 },
            animationSpec = tween(400),
        ) + fadeOut(),
    )

    val enterFromBottom = NavigationTransition(
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                400,
                easing = EaseOutQuart,
            ),
        ) + fadeIn(
            animationSpec = tween(300),
        ),

        exit = slideOutVertically(
            targetOffsetY = { -it / 3 },
            animationSpec = tween(400),
        ) + fadeOut(),
    )

    val enterFromTop = NavigationTransition(
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(
                400,
                easing = EaseOutQuart,
            ),
        ) + fadeIn(
            animationSpec = tween(300),
        ),

        exit = slideOutVertically(
            targetOffsetY = { it / 3 },
            animationSpec = tween(400),
        ) + fadeOut(),
    )

    val bottomBarFadeThrough = NavigationTransition(
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 180,
                easing = FastOutSlowInEasing,
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 90,
                easing = FastOutLinearInEasing,
            )
        ),
    )

    val bottomBarExpressive = NavigationTransition(
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 220,
                easing = FastOutSlowInEasing,
            )
        ) + scaleIn(
            initialScale = 0.96f,
            animationSpec = tween(
                durationMillis = 220,
                easing = FastOutSlowInEasing,
            ),
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 100,
                easing = FastOutLinearInEasing,
            ),
        ),
    )
}