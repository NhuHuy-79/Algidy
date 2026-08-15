package com.nhuhuy.algidy.core.designsystem.motion

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable

@Immutable
data class VisibilityTransition(
    val enter: EnterTransition,
    val exit: ExitTransition
)
