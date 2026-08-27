package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.harmonize
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.model.setting.SeedColor
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.animatedHorizontalShape
import com.nhuhuy.algidy.core.presentation.utils.toColor
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColorPresetPicker(
    modifier: Modifier = Modifier,
    itemPosition: ItemPosition,
    seedColor: SeedColor,
    enabled: Boolean = true,
    onSeedColorSelect: (color: SeedColor) -> Unit
) {
    val entries = SeedColor.entries
    ListItem(
        onClick = {},
        shapes = ListItemDefaults.shapes(shape = itemPosition.toVerticalSegmentedShape()),
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            leadingIconColor = MaterialTheme.colorScheme.onSurface
        ),
        supportingContent = {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                itemsIndexed(
                    items = entries,
                    key = { _, color: SeedColor -> color.name }
                ) { index: Int, item: SeedColor ->
                    val itemPosition = index.toItemPosition(entries.size)
                    val selected = enabled && item == seedColor
                    AppFilterButton(
                        modifier = Modifier.animateContentSize(),
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        shape = CircleShape,
                                        color = item.toColor().harmonize(
                                            other = MaterialTheme.colorScheme.primary
                                        )
                                    ),
                            )
                        },
                        enabled = enabled,
                        selected = selected,
                        label = item.name.capitalize(),
                        onClick = { onSeedColorSelect(item) },
                        shape = itemPosition.animatedHorizontalShape(selected),
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                        activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Color Preset",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "You must disable dynamic color to select color accent!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

