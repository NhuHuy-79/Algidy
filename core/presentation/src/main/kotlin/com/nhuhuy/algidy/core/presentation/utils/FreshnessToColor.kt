package com.nhuhuy.algidy.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.food.Freshness

@Composable
fun Freshness.toBackgroundColor(): Color {
    val color = AlgidyTheme.extendedColors
    return when (this) {
        Freshness.EXPIRED -> color.expired
        Freshness.URGENT -> color.notice
        Freshness.WARNING -> color.warning
        Freshness.FRESH -> color.fresh
    }
}

@Composable
fun Freshness.toContentColor(): Color {
    val color = AlgidyTheme.extendedColors
    return when (this) {
        Freshness.EXPIRED -> color.onExpired
        Freshness.URGENT -> color.onNotice
        Freshness.WARNING -> color.onWarning
        Freshness.FRESH -> color.onFresh
    }
}

@Composable
fun Freshness.toBackgroundContainerColor(): Color {
    val color = AlgidyTheme.extendedColors
    return when (this) {
        Freshness.EXPIRED -> color.expiredContainer
        Freshness.URGENT -> color.noticeContainer
        Freshness.WARNING -> color.warningContainer
        Freshness.FRESH -> color.freshContainer
    }
}

@Composable
fun Freshness.toContentContainerColor(): Color {
    val color = AlgidyTheme.extendedColors
    return when (this) {
        Freshness.EXPIRED -> color.onExpiredContainer
        Freshness.URGENT -> color.onNoticeContainer
        Freshness.WARNING -> color.onWarningContainer
        Freshness.FRESH -> color.onFreshContainer
    }
}