package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.nhuhuy.algidy.core.presentation.model.ImageProvider
import com.nhuhuy.algidy.core.presentation.model.toData

@Composable
fun FoodImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    imageProvider: ImageProvider = ImageProvider.Uri(imageUrl)
) {
    AsyncImage(
        modifier = modifier,
        model = imageProvider.toData(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
    )
}