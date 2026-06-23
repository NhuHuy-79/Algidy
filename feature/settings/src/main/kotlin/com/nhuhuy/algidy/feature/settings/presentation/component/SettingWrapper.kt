package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction

fun LazyListScope.dataSettingItem(
    modifier: Modifier = Modifier,
    items: List<SettingClickableItem>,
    onAction: (SettingsAction) -> Unit
) {
    items(
        count = items.size,
        key = { index -> "${items[index].type}" }
    ) { index ->

        val item = items[index]
        val icon = when (item.type) {
            ClickableType.Export -> Icons.Rounded.Upload
            is ClickableType.Import -> Icons.Rounded.Download
            ClickableType.DeleteAll -> ImageVector.vectorResource(
                com.nhuhuy.algidy.core.presentation.R.drawable.ic_delete
            )
            ClickableType.AboutApp -> Icons.Rounded.Info
            ClickableType.DailyReminder -> Icons.Rounded.Alarm
        }
        ClickableItem(
            modifier = modifier,
            item = item,
            icon = icon,
            position = index.ItemPosition(items.size),
            onClick = {
                onAction(SettingsAction.ClickableAction(type = item.type))
            }
        )
    }
}

