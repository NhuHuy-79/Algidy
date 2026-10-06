package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableUiModel

@Composable
fun ClickableItem(
    modifier: Modifier = Modifier,
    item: SettingClickableUiModel,
    shape: Shape,
    onClick: (SettingClickableUiModel) -> Unit,
) {
    ClickableSettingItem(
        modifier = modifier,
        icon = item.icon.toImageVector(),
        description = if (item.description != 0) stringResource(item.description) else "",
        title = stringResource(item.title),
        onClick = { onClick(item) },
        shape = shape
    )
}

@Composable
private fun ClickableSettingItem(
    modifier: Modifier = Modifier,
    shape: Shape,
    icon: ImageVector,
    description: String,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick),
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
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.basicMarquee()
            )
        },
    )
}