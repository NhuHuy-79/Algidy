package com.nhuhuy.algidy.feature.presentation.scanner.component

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ImageThumbnailButton(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(60.dp), // Kích thước thumbnail cố định
        shape = RoundedCornerShape(16.dp), // Bo góc Material 3 Expressive
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary), // Border nổi bật
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Staged image for scanning",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Cắt ảnh cho vừa khung
        )
    }
}