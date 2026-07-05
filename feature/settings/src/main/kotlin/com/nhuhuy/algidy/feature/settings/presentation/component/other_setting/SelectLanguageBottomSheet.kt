package com.nhuhuy.algidy.feature.settings.presentation.component.other_setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.presentation.R

@Composable
fun SelectLanguageBottomSheet(
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onLanguageSelect: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AppBottomSheet(
        onDismiss = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.setting_font),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Black
                    )
                )
            }

            itemsIndexed(
                items = AppLanguage.entries.toList(),
                key = { _: Int, item: AppLanguage -> item.name },
            ) { index: Int, language: AppLanguage ->

            }
        }
    }
}