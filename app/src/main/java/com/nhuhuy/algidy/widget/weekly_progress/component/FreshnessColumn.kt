@file:OptIn(ExperimentalGlancePreviewApi::class)

package com.nhuhuy.algidy.widget.weekly_progress.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.nhuhuy.algidy.core.designsystem.theme.DarkFoodStateColors
import com.nhuhuy.algidy.core.designsystem.theme.LightFoodStateColors
import com.nhuhuy.algidy.core.model.food.Freshness

@Composable
fun FreshnessColumn(
    freshness: Freshness,
    count: Int
) {
    val minHeight = 16
    val columnHeight = if (count < 1) 16.dp else (count * minHeight).dp.coerceAtMost(180.dp)
    val label = if (count > 10) "10+ " else "$count"
    Column(
        modifier = GlanceModifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = GlanceModifier.defaultWeight())
        Box(
            modifier = GlanceModifier
                .cornerRadius(24.dp)
                .height(columnHeight)
                .width(32.dp)
                .background(
                    day = freshness.toBackgroundLight(),
                    night = freshness.toBackgroundDark()
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(
                        day = freshness.toContentLight(),
                        night = freshness.toContentDark()
                    )
                )
            )
        }
    }
}

private fun Freshness.toBackgroundLight(): Color {
    LightFoodStateColors.apply {
        return when (this@toBackgroundLight) {
            Freshness.EXPIRED -> expiredContainer
            Freshness.URGENT -> noticeContainer
            Freshness.WARNING -> warningContainer
            Freshness.FRESH -> freshContainer
        }
    }
}

private fun Freshness.toContentLight(): Color {
    LightFoodStateColors.apply {
        return when (this@toContentLight) {
            Freshness.EXPIRED -> onExpiredContainer
            Freshness.URGENT -> onNoticeContainer
            Freshness.WARNING -> onWarningContainer
            Freshness.FRESH -> onFreshContainer
        }
    }
}

private fun Freshness.toBackgroundDark(): Color {
    DarkFoodStateColors.apply {
        return when (this@toBackgroundDark) {
            Freshness.EXPIRED -> expiredContainer
            Freshness.URGENT -> noticeContainer
            Freshness.WARNING -> warningContainer
            Freshness.FRESH -> freshContainer
        }
    }
}

private fun Freshness.toContentDark(): Color {
    DarkFoodStateColors.apply {
        return when (this@toContentDark) {
            Freshness.EXPIRED -> onExpiredContainer
            Freshness.URGENT -> onNoticeContainer
            Freshness.WARNING -> onWarningContainer
            Freshness.FRESH -> onFreshContainer
        }
    }
}

