package com.nhuhuy.algidy.widget.weekly_expiry.component

import androidx.compose.runtime.Composable
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import com.nhuhuy.algidy.widget.model.fakeExpiryFoodList
import com.nhuhuy.algidy.widget.weekly_expiry.WeekExpiryLargeWidget

@OptIn(ExperimentalGlancePreviewApi::class)
@Composable
@Preview(widthDp = 320, heightDp = 110)
fun Preview() {
    WeekExpiryLargeWidget(
        expiryFoodModels = fakeExpiryFoodList,
        onConsume = {}
    )
}