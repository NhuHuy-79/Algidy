package com.nhuhuy.algidy.core.designsystem.icon

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    iconProvider: IconProvider,
    description: String? = null,
    tint: Color = LocalContentColor.current
) {
    Icon(
        modifier = modifier,
        imageVector = iconProvider.toImageVector(),
        contentDescription = description,
        tint = tint
    )
}