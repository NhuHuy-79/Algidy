package com.nhuhuy.algidy.core.designsystem.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Immutable

@Immutable
data class NavTransitionSpec(
    val transitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform,
    val popTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform,
    val predictivePopTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform,
)