package com.nhuhuy.algidy.widget

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import com.nhuhuy.algidy.widget.model.fakeExpiryFoodList
import com.nhuhuy.algidy.widget.utils.WidgetColorScheme
import com.nhuhuy.algidy.widget.weekly_expiry.WeekExpiryLargeWidget

@OptIn(ExperimentalGlancePreviewApi::class)
@Composable
@Preview(widthDp = 220, heightDp = 260)
@Preview(widthDp = 300, heightDp = 260)
@Preview(widthDp = 375, heightDp = 260)
@Preview(widthDp = 360, heightDp = 240)
fun Preview() {
    GlanceTheme(WidgetColorScheme.colors) {
        Box(
            modifier = GlanceModifier.fillMaxSize()
                .background(colorProvider = WidgetColorScheme.colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            WeekExpiryLargeWidget(
                expiryFoodModels = fakeExpiryFoodList,
                onConsume = {}
            )
        }
    }
}