package com.nhuhuy.algidy.feature.settings.presentation.component.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.model.setting.ThemeMode
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.animatedHorizontalShape
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.settings.presentation.component.ListItemContent
import com.nhuhuy.algidy.feature.settings.utils.toStringRes

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemePicker(
    itemPosition: ItemPosition,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val algidyIcons = AlgidyIcons.Settings
    val icon = when (themeMode) {
        ThemeMode.LIGHT -> algidyIcons.LightTheme
        ThemeMode.DARK -> algidyIcons.DarkTheme
        ThemeMode.SYSTEM -> algidyIcons.SystemTheme
    }

    val entries = ThemeMode.entries

    ListItemContent(
        title = stringResource(R.string.setting_app_theme),
        modifier = Modifier.fillMaxWidth(),
        icon = icon.toImageVector(),
        shape = itemPosition.toVerticalSegmentedShape(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            entries.forEachIndexed { index, mode ->
                val itemPosition = index.toItemPosition(entries.size)
                val selected = themeMode == mode
                AppFilterButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(mode.toStringRes()),
                    selected = selected,
                    onClick = { onThemeModeChange(mode) },
                    shape = itemPosition.animatedHorizontalShape(selected = selected),
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}