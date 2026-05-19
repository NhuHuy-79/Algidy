package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.nhuhuy.algidy.core.designsystem.R
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toRoundedCornerShape

@Composable
fun ToggleableSettingItem(
    position: ItemPosition,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    text: String,
    title: String,
    onToggleClick: (enable: Boolean) -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .clip(position.toRoundedCornerShape()),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        },
        supportingContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        },
        trailingContent = {
            Switch(
                checked = enabled,
                onCheckedChange = onToggleClick,
                thumbContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_check_small),
                        contentDescription = null
                    )
                }
            )
        }
    )
}

@Preview
@Composable
private fun ToggleableSettingItemPreview() {
    AlgidyTheme {
        ToggleableSettingItem(
            position = ItemPosition.SINGLE,
            title = "Notifications",
            text = "Receive alerts for expiring food",
            enabled = true,
            onToggleClick = {}
        )
        /*
                ClickableSettingItem(
                    position = ItemPosition.SINGLE,
                    title = "Export Data",
                    text = "Export your data",
                    icon = Icons.Rounded.Upload,
                    onClick = {}
                )*/
    }
}

@Composable
fun ClickableSettingItem(
    position: ItemPosition,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .clip(position.toRoundedCornerShape())
            .clickable(
                onClick = onClick
            ),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.labelMedium
            )
        },
    )
}