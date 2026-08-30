package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.feature.settings.data.AuthorConstant
import com.nhuhuy.algidy.feature.settings.presentation.component.ClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.component.about_app.AlgidyMainContent
import com.nhuhuy.algidy.feature.settings.presentation.component.about_app.AuthorContent
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingItems
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsUiState
import com.nhuhuy.algidy.feature.settings.utils.openUrl
import com.nhuhuy.algidy.feature.settings.utils.sendEmail

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutAppScreen(
    uiState: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
) {
    val items = SettingItems.AboutScreen
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.setting_about_app),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = "Algidy v${uiState.versionName}",
                    )
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
                .padding(padding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                AlgidyMainContent(
                    modifier = Modifier.fillMaxWidth(),
                    itemPosition = ItemPosition.TOP,
                    appVersion = uiState.versionName,
                    onGithubClick = { onAction(SettingsAction.OnGithubClick) },
                    onDiscordClick = {}
                )
            }

            item {
                AuthorContent(
                    itemPosition = ItemPosition.BOTTOM,
                    modifier = Modifier.fillMaxWidth(),
                    onGithubClick = { context.openUrl(AuthorConstant.GITHUB) },
                    onEmailClick = { context.sendEmail(AuthorConstant.EMAIL) },
                    onLinkedlnClick = { context.openUrl(AuthorConstant.LINKEDIN) },
                )
            }

            item { Spacer(modifier = Modifier.height(22.dp)) }

            itemsIndexed(
                items = items
            ) { index, item ->
                ClickableItem(
                    item = item,
                    position = index.toItemPosition(items.size),
                    onClick = { onAction(SettingsAction.ClickableAction(it)) }
                )
            }
        }
    }
}
