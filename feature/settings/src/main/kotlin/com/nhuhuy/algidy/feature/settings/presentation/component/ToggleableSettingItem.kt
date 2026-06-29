package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowOutward
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.R
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toRoundedCornerShape

@Composable
fun ToggleableSettingItem(
    position: ItemPosition,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = false,
    text: String,
    title: String,
    onToggleClick: (enable: Boolean) -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .clip(position.toRoundedCornerShape()),
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
                style = MaterialTheme.typography.labelLarge
            )
        },
        trailingContent = {
            Switch(
                enabled = enabled,
                checked = checked,
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
            checked = true,
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedShape by animateDpAsState(
        targetValue = if (isPressed) 24.dp else 8.dp
    )
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .clip(
                position.toRoundedCornerShape(
                    small = animatedShape
                )
            )
            .clickable(
                interactionSource = interactionSource,
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
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Rounded.ArrowOutward,
                contentDescription = null
            )
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.labelLarge
            )
        },
    )
}