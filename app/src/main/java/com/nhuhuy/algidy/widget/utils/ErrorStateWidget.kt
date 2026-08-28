package com.nhuhuy.algidy.widget.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import com.nhuhuy.algidy.core.presentation.R

@Composable
fun ErrorStateWidget() {
    Column(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.widget_error),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}