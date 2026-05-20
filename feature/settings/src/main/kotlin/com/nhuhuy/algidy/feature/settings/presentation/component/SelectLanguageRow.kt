package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.toDisplayName

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
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppLanguage.entries.forEach { language ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 4.dp),
                selected = language == currentLanguage,
                onClick = { onLanguageSelected(language) },
                label = {
                    Text(text = language.toDisplayName())
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
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppFont.entries.forEach { font ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 4.dp),
                selected = font == currentFont,
                onClick = { onFontSelected(font) },
                label = {
                    Text(text = font.fontName)
                },
            )
        }
    }
}