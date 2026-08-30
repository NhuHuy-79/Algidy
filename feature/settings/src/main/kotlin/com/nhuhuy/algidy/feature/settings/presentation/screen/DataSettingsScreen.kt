package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingItems
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DataSettingsScreen(
    snackBarHost: SnackbarHostState,
    onAction: (SettingsAction) -> Unit,
) {
    val items = SettingItems.DataScreen
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHost,
                modifier = Modifier.padding(bottom = 72.dp)
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.your_data_title),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(text = stringResource(R.string.your_data_subtitle))
                },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = { onAction(SettingsAction.OnBackClick) }
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(
                items = items,
            ) { index, item ->
                ClickableItem(
                    modifier = Modifier.fillMaxWidth(),
                    item = item,
                    position = index.toItemPosition(items.size),
                    onClick = { onAction(SettingsAction.ClickableAction(it)) }
                )
            }
        }
    }
}
