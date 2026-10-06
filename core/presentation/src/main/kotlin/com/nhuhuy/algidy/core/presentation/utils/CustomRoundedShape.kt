package com.nhuhuy.algidy.core.presentation.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


enum class ItemPosition {
    TOP, MIDDLE, SINGLE, BOTTOM
}

fun verticalRoundedCornerShape(
    top: Dp,
    bottom: Dp
) = RoundedCornerShape(
    topStart = top,
    topEnd = top,
    bottomEnd = bottom,
    bottomStart = bottom
)

fun Int.toItemPosition(sizeList: Int): ItemPosition =
    when {
        sizeList <= 1 -> ItemPosition.SINGLE
        this == 0 -> ItemPosition.TOP
        this == sizeList - 1 -> ItemPosition.BOTTOM
        else -> ItemPosition.MIDDLE
    }


fun ItemPosition.toVerticalSegmentedShape(
    large: Dp = 16.dp, small: Dp = 8.dp
): RoundedCornerShape {
    return when (this) {
        ItemPosition.TOP -> verticalRoundedCornerShape(top = large, bottom = small)
        ItemPosition.MIDDLE -> RoundedCornerShape(small)
        ItemPosition.SINGLE -> RoundedCornerShape(large)
        ItemPosition.BOTTOM -> verticalRoundedCornerShape(top = small, bottom = large)
    }
}
