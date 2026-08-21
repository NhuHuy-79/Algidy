package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.model.setting.SeedColor
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toColor
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColorPresetPicker(
    modifier: Modifier = Modifier,
    seedColor: SeedColor,
    enabled: Boolean = true,
    onSeedColorSelect: (color: SeedColor) -> Unit
) {
    ListItem(
        onClick = {},
        shapes = ListItemDefaults.shapes(
            shape = ItemPosition.BOTTOM.toVerticalSegmentedShape()
        ),
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            leadingIconColor = MaterialTheme.colorScheme.onSurface
        ),
        supportingContent = {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SeedColor.entries.forEach { color ->
                    AppFilterButton(
                        modifier = Modifier.animateContentSize(),
                        leadingContent = {
                            if (enabled) {
                                Surface(
                                    modifier = Modifier.size(8.dp),
                                    shape = CircleShape,
                                    color = color.toColor(),
                                ) { }
                            }
                        },
                        enabled = enabled,
                        selected = enabled && color == seedColor,
                        label = color.name.capitalize(),
                        onClick = { onSeedColorSelect(color) }
                    )
                }
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Color Preset",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!enabled) {
                Text(
                    text = "You must disable dynamic color to select color accent!",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

