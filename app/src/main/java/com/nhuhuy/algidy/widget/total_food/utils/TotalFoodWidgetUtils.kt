package com.nhuhuy.algidy.widget.total_food.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.unit.ColorProvider
import com.nhuhuy.algidy.core.designsystem.theme.LightFoodStateColors
import com.nhuhuy.algidy.core.model.food.Freshness

object TotalFoodWidgetUtils {
    val MIN_HEIGHT = 24.dp
}

@Composable
fun Freshness.toColor(): ColorProvider {
    return when (this) {
        Freshness.EXPIRED -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.expired,
            night = LightFoodStateColors.expired
        )

        Freshness.URGENT -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.notice,
            night = LightFoodStateColors.notice
        )

        Freshness.WARNING -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.warning,
            night = LightFoodStateColors.warning
        )

        Freshness.FRESH -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.fresh,
            night = LightFoodStateColors.fresh
        )
    }
}

@Composable
fun Freshness.toContainerColor(): ColorProvider {
    return when (this) {
        Freshness.EXPIRED -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.expiredContainer,
            night = LightFoodStateColors.expiredContainer
        )

        Freshness.URGENT -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.noticeContainer,
            night = LightFoodStateColors.noticeContainer
        )

        Freshness.WARNING -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.warningContainer,
            night = LightFoodStateColors.warningContainer
        )

        Freshness.FRESH -> androidx.glance.color.ColorProvider(
            day = LightFoodStateColors.freshContainer,
            night = LightFoodStateColors.freshContainer
        )
    }

}