package com.nhuhuy.algidy.feature.scanner.presentation.component

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
    onAutoScanChange: (Boolean) -> Unit,
    onProcessImageClick: (Uri) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Đẩy Gallery/Thumbnail sang trái, AutoScan sang phải
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(72.dp), // Container cố định để tránh nhảy layout
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = stagedImageUri,
                transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                label = "ThumbnailTransition"
            ) { uri ->
                if (uri != null) {
                    ImageThumbnailButton(
                        imageUri = uri,
                        onClick = { onProcessImageClick(uri) }
                    )
                } else {
                    SelectImageButton(
                        modifier = Modifier.size(56.dp),
                        onClick = onSelectImageClick,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

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