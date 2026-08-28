package com.nhuhuy.algidy.widget.weekly_progress.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.width
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.widget.model.FreshnessStatisticModel

@Composable
fun TotalFoodContent(
    modifier: GlanceModifier = GlanceModifier,
    freshnessStatisticModel: FreshnessStatisticModel,
) {
    val spacing = 12.dp
    Row(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.Bottom
    ) {
        TotalFoodItem(
            freshness = Freshness.FRESH,
            value = freshnessStatisticModel.freshCount,
            percent = freshnessStatisticModel.freshPercent
        )

        Spacer(modifier = GlanceModifier.width(spacing))

        TotalFoodItem(
            freshness = Freshness.WARNING,
            value = freshnessStatisticModel.noticeCount,
            percent = freshnessStatisticModel.noticePercent
        )

        Spacer(modifier = GlanceModifier.width(spacing))

        TotalFoodItem(
            freshness = Freshness.URGENT,
            value = freshnessStatisticModel.urgentCount,
            percent = freshnessStatisticModel.urgentPercent
        )

        Spacer(modifier = GlanceModifier.width(spacing))

        TotalFoodItem(
            freshness = Freshness.EXPIRED,
            value = freshnessStatisticModel.expiredCount,
            percent = freshnessStatisticModel.expiredPercent
        )
    }
}