package com.nhuhuy.algidy.transition

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Stable

@Stable
data class NavigationTransition(
    val enter: EnterTransition,
    val exit: ExitTransition,
)

fun NavigationTransition.toContentTransform(): ContentTransform {
    return this.enter togetherWith this.exit
}