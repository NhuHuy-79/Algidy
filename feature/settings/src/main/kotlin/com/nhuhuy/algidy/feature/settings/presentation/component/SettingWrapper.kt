package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction

fun LazyListScope.otherSettingItems(
    items: List<SettingToggleItem>,
    onAction: (SettingsAction) -> Unit
) {
    item {
        Text(
            text = stringResource(R.string.setting_other_settings_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

    items(
        count = items.size,
        key = { index -> "${items[index].type}" }
    ) { index ->
        val item = items[index]
        ToggleItem(
            position = index.ItemPosition(items.size),
            item = item,
            onToggle = { enabled, item ->
                onAction(
                    SettingsAction.ToggleAction(
                        type = item.type,
                        enabled = enabled
                    )
                )
            }
        )
    }
}
fun LazyListScope.dataSettingItem(
    modifier: Modifier = Modifier,
    items: List<SettingClickableItem>,
    onAction: (SettingsAction) -> Unit,
    onOutsideAction: () -> Unit
) {

    item {
        Text(
            text = stringResource(R.string.setting_data),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

    items(
        count = items.size,
        key = { index -> "${items[index].type}" }
    ) { index ->

        val item = items[index]
        val icon = when (item.type) {
            ClickableType.Export -> Icons.Rounded.Upload
            is ClickableType.Import -> Icons.Rounded.Download
            ClickableType.DeleteAll -> Icons.Rounded.DeleteForever
            ClickableType.AboutApp -> Icons.Rounded.Info
        }
        ClickableItem(
            modifier = modifier,
            item = item,
            icon = icon,
            position = index.ItemPosition(items.size),
            onClick = {
                onAction(SettingsAction.ClickableAction(type = item.type))

                if (item.type is ClickableType.Import) {
                    onOutsideAction()
                }
            }
        )
    }
}

