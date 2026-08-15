package com.nhuhuy.algidy.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Immutable
sealed interface IconProvider {
    data class ImageVectorIcon(
        val imageVector: ImageVector,
    ) : IconProvider

    data class DrawableResourceIcon(
        @field:DrawableRes val id: Int,
    ) : IconProvider
}

@Composable
fun IconProvider.toImageVector(): ImageVector {
    return when (this) {
        is IconProvider.ImageVectorIcon -> imageVector
        is IconProvider.DrawableResourceIcon -> ImageVector.vectorResource(id)
    }
}