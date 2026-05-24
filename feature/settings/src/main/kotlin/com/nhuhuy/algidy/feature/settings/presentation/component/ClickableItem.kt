package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableItem

@Composable
fun ClickableItem(
    modifier: Modifier = Modifier,
    item: SettingClickableItem,
    icon: ImageVector,
    position: ItemPosition,
    onClick: (SettingClickableItem) -> Unit,
) {

    val title = when (item.type) {
        ClickableType.Export -> stringResource(R.string.setting_export)
        is ClickableType.Import -> stringResource(R.string.setting_import)
        ClickableType.DeleteAll -> stringResource(R.string.setting_clear_data)
        ClickableType.AboutApp -> stringResource(R.string.setting_about_app)
    }

    val desc = when (item.type) {
        ClickableType.Export -> stringResource(R.string.setting_export_desc)
        is ClickableType.Import -> stringResource(R.string.setting_import_desc)
        ClickableType.DeleteAll -> stringResource(R.string.setting_clear_data_desc)
        ClickableType.AboutApp -> stringResource(R.string.setting_about_app_desc)
    }

    ClickableSettingItem(
        modifier = modifier,
        position = position,
        icon = icon,
        description = desc,
        title = title,
        onClick = { onClick(item) },
    )
}