package com.nhuhuy.algidy.widget.weekly_expiry.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.layout.padding
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.widget.utils.WidgetColors
import com.nhuhuy.algidy.widget.utils.toColorProvider

@Composable
fun WeeklyExpirySmallTopBar(
    modifier: GlanceModifier
) {
    TitleBar(
        modifier = modifier,
        startIcon = ImageProvider(AlgidyIcons.Widget.WeekExpiryIcon.id),
        title = LocalContext.current.getString(R.string.widget_weekly_food_title),
        iconColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
        textColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
    )
}

@Composable
fun WeeklyExpiryMediumTopBar(
    modifier: GlanceModifier = GlanceModifier
) {
    TitleBar(
        modifier = modifier.padding(end = 12.dp),
        startIcon = ImageProvider(AlgidyIcons.Widget.WeekExpiryIcon.id),
        title = LocalContext.current.getString(R.string.widget_weekly_food_title),
        iconColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
        textColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
    ) {
        Image(
            provider = ImageProvider(AlgidyIcons.Widget.RefreshData.id),
            contentDescription = null,
            colorFilter = ColorFilter.tint(WidgetColors.ON_BACKGROUND.toColorProvider())
        )
    }
}

@Composable
fun WeeklyExpiryLargeTopBar(
    modifier: GlanceModifier = GlanceModifier
) {
    TitleBar(
        modifier = modifier.padding(end = 12.dp),
        startIcon = ImageProvider(AlgidyIcons.Widget.WeekExpiryIcon.id),
        title = LocalContext.current.getString(R.string.widget_weekly_food_title),
        iconColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
        textColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
    ) {
        Image(
            provider = ImageProvider(AlgidyIcons.Widget.RefreshData.id),
            contentDescription = null,
            colorFilter = ColorFilter.tint(WidgetColors.ON_BACKGROUND.toColorProvider())
        )
    }
}
