package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableUiModel
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingItems
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainSettingsScreen(
    onAction: (SettingsAction) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.main_settings_title_page),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.main_settings_subtitle),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 96.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val section1 = SettingItems.MainScreenSection1
            val section2 = SettingItems.MainScreenSection2
            itemsIndexed(section1) { index: Int, item: SettingClickableUiModel ->
                ClickableItem(
                    item = item,
                    position = index.toItemPosition(section1.size),
                    onClick = { onAction(SettingsAction.ClickableAction(it)) }
                )

                if (index != section1.lastIndex) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            itemsIndexed(section2) { index: Int, item: SettingClickableUiModel ->
                ClickableItem(
                    item = item,
                    position = index.toItemPosition(section2.size),
                    onClick = { onAction(SettingsAction.ClickableAction(it)) }
                )

                if (index != section1.lastIndex) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}
