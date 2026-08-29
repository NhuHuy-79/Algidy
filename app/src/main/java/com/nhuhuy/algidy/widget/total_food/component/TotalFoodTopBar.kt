package com.nhuhuy.algidy.widget.total_food.component

import androidx.compose.runtime.Composable
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.components.TitleBar
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.widget.utils.WidgetColors
import com.nhuhuy.algidy.widget.utils.toColorProvider

@Composable
fun TotalFoodTopBar() {
    TitleBar(
        startIcon = ImageProvider(AlgidyIcons.Widget.TotalFood.id),
        title = LocalContext.current.getString(com.nhuhuy.algidy.core.presentation.R.string.widget_weekly_freshness_title),
        iconColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
        textColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
    )
}


/*
fun TotalFoodMediumTopBar(){
    TitleBar(
        startIcon = ImageProvider(AlgidyIcons.Widget.TotalFood.id),
        title = "Total Food",
        textColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
        iconColor = WidgetColors.ON_BACKGROUND.toColorProvider()
    )
}

@Composable
fun TotalFoodLargeTopBar(){
    TitleBar(
        startIcon = ImageProvider(AlgidyIcons.Widget.TotalFood.id),
        title = "Total Food",
        textColor = WidgetColors.ON_BACKGROUND.toColorProvider(),
        iconColor = WidgetColors.ON_BACKGROUND.toColorProvider()
    )
}*/
