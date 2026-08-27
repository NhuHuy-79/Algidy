package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableUiModel

@Composable
fun ClickableItem(
    modifier: Modifier = Modifier,
    item: SettingClickableUiModel,
    position: ItemPosition,
    onClick: (SettingClickableUiModel) -> Unit,
) {
    ClickableSettingItem(
        modifier = modifier,
        position = position,
        icon = item.icon.toImageVector(),
        description = if (item.description != 0) stringResource(item.description) else "",
        title = stringResource(item.title),
        onClick = { onClick(item) },
    )
}

@Composable
private fun ClickableSettingItem(
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .clip(
                position.toVerticalSegmentedShape(small = animatedShape)
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
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        },
        trailingContent = {
            Icon(
                imageVector = AlgidyIcons.Settings.ArrowForward.toImageVector(),
                contentDescription = null
            )
        },
        supportingContent = {
            Text(
                text = description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge
            )
        },
    )
}