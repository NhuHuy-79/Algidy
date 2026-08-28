package com.nhuhuy.algidy.widget.weekly_expiry.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.widget.model.ExpiryFoodModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WeeklySmallExpiryContent(
    expiryFoodModels: ImmutableList<ExpiryFoodModel>,
    modifier: GlanceModifier = GlanceModifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = expiryFoodModels,
            itemId = { _: Int, item: ExpiryFoodModel -> item.id.hashCode().toLong() }
        ) { index: Int, item: ExpiryFoodModel ->
            val itemPosition = index.toItemPosition(expiryFoodModels.size)
            Box(
                modifier = GlanceModifier.padding(bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                WeekExpirySmallFoodItem(
                    item = item,
                    itemPosition = itemPosition,
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun WeeklyMediumExpiryContent(
    expiryFoodModels: ImmutableList<ExpiryFoodModel>,
    modifier: GlanceModifier = GlanceModifier,
    onConsume: (id: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = expiryFoodModels,
            itemId = { _: Int, item: ExpiryFoodModel -> item.id.hashCode().toLong() }
        ) { index: Int, item: ExpiryFoodModel ->
            val itemPosition = index.toItemPosition(expiryFoodModels.size)
            Box(
                modifier = GlanceModifier.padding(bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                WeekExpiryMediumFoodItem(
                    item = item,
                    itemPosition = itemPosition,
                    modifier = GlanceModifier.fillMaxWidth(),
                    onConsume = { onConsume(item.id) }
                )
            }
        }
    }
}

@Composable
fun WeeklyLargeExpiryContent(
    expiryFoodModels: ImmutableList<ExpiryFoodModel>,
    onConsume: (id: String) -> Unit,
    modifier: GlanceModifier = GlanceModifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = expiryFoodModels,
            itemId = { _: Int, item: ExpiryFoodModel -> item.id.hashCode().toLong() }
        ) { index: Int, item: ExpiryFoodModel ->
            val itemPosition = index.toItemPosition(expiryFoodModels.size)
            Box(
                modifier = GlanceModifier.padding(bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                WeekExpiryLargeFoodItem(
                    onConsume = { onConsume(item.id) },
                    item = item,
                    itemPosition = itemPosition,
                    modifier = GlanceModifier.fillMaxWidth(),
                )
            }

        }
    }
}