package com.nhuhuy.algidy.feature.settings.presentation.component.other_setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toRoundedCornerShape

@Composable
fun SelectLanguageBottomSheet(
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onLanguageSelect: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AppBottomSheet(
        modifier = Modifier.padding(16.dp),
        onDismiss = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.setting_language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            itemsIndexed(
                items = AppLanguage.entries,
                key = { _: Int, item: AppLanguage -> item.name },
            ) { index: Int, language: AppLanguage ->
                val itemPosition = index.toItemPosition(AppLanguage.entries.size)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (language == currentLanguage) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = if (language == currentLanguage) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
                    shape = itemPosition.toRoundedCornerShape(),
                    onClick = { onLanguageSelect(language) },
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = language.name.capitalize(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}