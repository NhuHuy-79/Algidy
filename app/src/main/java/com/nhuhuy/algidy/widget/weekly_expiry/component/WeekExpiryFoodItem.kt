package com.nhuhuy.algidy.widget.weekly_expiry.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.components.SquareIconButton
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.Text
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.widget.model.ExpiryFoodModel
import com.nhuhuy.algidy.widget.utils.MAX_LENGTH_LARGE
import com.nhuhuy.algidy.widget.utils.MAX_LENGTH_MEDIUM
import com.nhuhuy.algidy.widget.utils.MAX_LENGTH_SMALL
import com.nhuhuy.algidy.widget.utils.WidgetColors
import com.nhuhuy.algidy.widget.utils.WidgetLayoutConfig
import com.nhuhuy.algidy.widget.utils.WidgetTypography
import com.nhuhuy.algidy.widget.utils.toColorProvider
import com.nhuhuy.algidy.widget.utils.toVerticalDrawable
import com.nhuhuy.algidy.widget.utils.truncateForWidget

@Composable
fun WeekExpirySmallFoodItem(
    modifier: GlanceModifier,
    item: ExpiryFoodModel,
    itemPosition: ItemPosition,
) {
    val resId = itemPosition.toVerticalDrawable()

    Row(
        modifier = modifier
            .height(48.dp)
            .background(
                imageProvider = ImageProvider(resId),
                colorFilter = ColorFilter.tint(
                    colorProvider = WidgetColors.LIST_ITEM_BACKGROUND.toColorProvider()
                )
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemTextContent(
            name = item.name,
            storageLocation = item.storageLocation,
            widgetMode = WidgetLayoutConfig.WidgetMode.COMPACT
        )
    }
}

@Composable
fun WeekExpiryMediumFoodItem(
    modifier: GlanceModifier,
    item: ExpiryFoodModel,
    itemPosition: ItemPosition,
    onConsume: () -> Unit
) {
    val resId = itemPosition.toVerticalDrawable()

    Row(
        modifier = modifier
            .wrapContentHeight()
            .background(
                imageProvider = ImageProvider(resId),
                colorFilter = ColorFilter.tint(
                    colorProvider = WidgetColors.LIST_ITEM_BACKGROUND.toColorProvider()
                )
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemTextContent(
            name = item.name,
            storageLocation = item.storageLocation,
            widgetMode = WidgetLayoutConfig.WidgetMode.MEDIUM
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        SquareIconButton(
            imageProvider = ImageProvider(AlgidyIcons.Widget.ConsumedFood.id),
            contentDescription = null,
            onClick = onConsume,
            backgroundColor = WidgetColors.LIST_ITEM_BACKGROUND.toColorProvider(),
            contentColor = WidgetColors.LIST_ITEM_CONTENT.toColorProvider()
        )
    }
}

@Composable
fun WeekExpiryLargeFoodItem(
    modifier: GlanceModifier,
    item: ExpiryFoodModel,
    itemPosition: ItemPosition,
    onConsume: () -> Unit
) {
    val resId = itemPosition.toVerticalDrawable()

    Row(
        modifier = modifier
            .wrapContentHeight()
            .background(
                imageProvider = ImageProvider(resId),
                colorFilter = ColorFilter.tint(
                    colorProvider = WidgetColors.LIST_ITEM_BACKGROUND.toColorProvider()
                )
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemTextContent(
            name = item.name,
            storageLocation = item.storageLocation,
            widgetMode = WidgetLayoutConfig.WidgetMode.EXPANDED
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        SquareIconButton(
            imageProvider = ImageProvider(AlgidyIcons.Widget.ConsumedFood.id),
            contentDescription = null,
            onClick = onConsume,
            backgroundColor = WidgetColors.LIST_ITEM_BACKGROUND.toColorProvider(),
            contentColor = WidgetColors.LIST_ITEM_CONTENT.toColorProvider()
        )
    }
}

@Composable
private fun ItemTextContent(
    name: String,
    storageLocation: StorageLocation,
    widgetMode: WidgetLayoutConfig.WidgetMode,
) {
    val maxLength = when (widgetMode) {
        WidgetLayoutConfig.WidgetMode.COMPACT -> MAX_LENGTH_SMALL
        WidgetLayoutConfig.WidgetMode.MEDIUM -> MAX_LENGTH_MEDIUM
        WidgetLayoutConfig.WidgetMode.EXPANDED -> MAX_LENGTH_LARGE
    }

    Column {
        Text(
            text = name.truncateForWidget(maxLength),
            style = WidgetTypography.body.copy(
                color = WidgetColors.LIST_ITEM_CONTENT.toColorProvider()
            ),
        )

        Spacer(modifier = GlanceModifier.height(2.dp))

        Text(
            text = LocalContext.current.getString(storageLocation.toStringRes()),
            style = WidgetTypography.labelSmall.copy(
                color = WidgetColors.LIST_ITEM_CONTENT.toColorProvider()
            )
        )
    }
}