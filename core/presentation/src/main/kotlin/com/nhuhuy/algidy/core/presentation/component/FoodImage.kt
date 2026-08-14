package com.nhuhuy.algidy.core.presentation.component


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun FoodImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
) {
    AsyncImage(
        modifier = modifier,
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
}