package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.designsystem.R
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleUiModel

@Composable
fun ToggleItem(
    modifier: Modifier = Modifier,
    item: SettingToggleUiModel,
    shape: Shape,
    onToggle: (enable: Boolean, item: SettingToggleUiModel) -> Unit
) {
    ToggleableSettingItem(
        modifier = modifier,
        checked = item.checked,
        enabled = item.enabled,
        text = stringResource(item.description),
        title = stringResource(item.title),
        shape = shape,
        onToggleClick = { enabled -> onToggle(enabled, item) }
    )
}

@Composable
private fun ToggleableSettingItem(
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = false,
    text: String,
    title: String,
    onToggleClick: (enable: Boolean) -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.clip(shape),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        },
        supportingContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            Switch(
                enabled = enabled,
                checked = checked,
                onCheckedChange = onToggleClick,
                thumbContent = {
                    if (checked) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_check_small),
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_close_small),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    )
}