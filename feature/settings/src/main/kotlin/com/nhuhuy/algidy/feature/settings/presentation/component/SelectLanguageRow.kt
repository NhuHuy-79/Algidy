package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.feature.settings.utils.toStringRes

@Composable
fun SelectLanguageRow(
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AppLanguage.entries.sortedBy { language -> language.name }
            .forEach { language ->
            FilterChip(
                modifier = Modifier,
                selected = language == currentLanguage,
                onClick = { onLanguageSelected(language) },
                label = {
                    Text(text = stringResource(language.toStringRes()))
                },
            )
        }
    }
}

@Composable
fun SelectFontRow(
    currentFont: AppFont,
    modifier: Modifier = Modifier,
    onFontSelected: (AppFont) -> Unit
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AppFont.entries
            .sortedBy { font -> font.fontName.length }
            .forEach { font ->
            FilterChip(
                modifier = Modifier,
                selected = font == currentFont,
                onClick = { onFontSelected(font) },
                label = {
                    Text(text = font.fontName)
                },
            )
        }
    }
}