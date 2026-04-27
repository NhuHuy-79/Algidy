package com.nhuhuy.algidy.feature.presentation.scanner.component

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScannerControlBar(
    modifier: Modifier = Modifier,
    isAutoScanned: Boolean,
    stagedImageUri: Uri?,
    onSelectImageClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onAutoScanChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectImageButton(
            modifier = Modifier.size(56.dp),
            onClick = onSelectImageClick,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.primary
        )

        CaptureButton(
            modifier = Modifier.size(96.dp),
            onCapturePress = if (stagedImageUri == null) onCaptureClick else ({}),
            enable = stagedImageUri == null,
            contentColor = MaterialTheme.colorScheme.primary
        )

        AutoScanButton(
            modifier = Modifier.size(56.dp),
            autoScanning = isAutoScanned,
            onClick = onAutoScanChange,
            enableContainerColor = MaterialTheme.colorScheme.primary,
            disableContainerColor = MaterialTheme.colorScheme.onSurface
        )
    }
}