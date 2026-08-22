package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.animatedHorizontalShape
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape

@Composable
fun StorageLocationField(
    itemPosition: ItemPosition,
    currentLocation: StorageLocation,
    onLocationSelect: (location: StorageLocation) -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current
    val entries = StorageLocation.entries

    ToggleListItem(
        modifier = Modifier
            .fillMaxWidth(),
        shape = itemPosition.toVerticalSegmentedShape(),
        title = stringResource(R.string.food_entry_storage_location),
        icon = AlgidyIcons.FoodEntry.StorageLocation.toImageVector(),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
        ) {
            itemsIndexed(
                items = entries,
                key = { _, item -> item.ordinal }
            ) { index: Int, item: StorageLocation ->
                val itemPosition = index.toItemPosition(entries.size)
                val selected = item == currentLocation
                AppFilterButton(
                    selected = item == currentLocation,
                    label = stringResource(item.toStringRes()),
                    onClick = { onLocationSelect(item) },
                    shape = itemPosition.animatedHorizontalShape(selected = selected)
                )
            }
        }
    }
}