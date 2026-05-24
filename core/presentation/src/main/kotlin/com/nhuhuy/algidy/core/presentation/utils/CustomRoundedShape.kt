package com.nhuhuy.algidy.core.presentation.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


enum class ItemPosition {
    TOP, MIDDLE, SINGLE, BOTTOM
}

fun horizontalRoundedCornerShape(
    start: Dp,
    end: Dp
) = RoundedCornerShape(
    topStart = start,
    bottomStart = start,
    topEnd = end,
    bottomEnd = end
)

fun verticalRoundedCornerShape(
    top: Dp,
    bottom: Dp
) = RoundedCornerShape(
    topStart = top,
    topEnd = top,
    bottomEnd = bottom,
    bottomStart = bottom
)

fun Int.ItemPosition(
    sizeList: Int
): ItemPosition {
    val position = when {
        this == 0 -> ItemPosition.TOP
        this == sizeList - 1 -> ItemPosition.BOTTOM
        else -> ItemPosition.MIDDLE
    }
    return position
}

fun ItemPosition.toRoundedCornerShape(
    large: Dp = 16.dp, small: Dp = 4.dp
): RoundedCornerShape {
    return when (this) {
        ItemPosition.TOP -> verticalRoundedCornerShape(top = large, bottom = small)
        ItemPosition.MIDDLE -> RoundedCornerShape(small)
        ItemPosition.SINGLE -> RoundedCornerShape(large)
        ItemPosition.BOTTOM -> verticalRoundedCornerShape(top = small, bottom = large)
    }
}
