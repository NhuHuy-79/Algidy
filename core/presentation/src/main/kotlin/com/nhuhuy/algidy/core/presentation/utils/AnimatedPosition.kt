package com.nhuhuy.algidy.core.presentation.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ItemPosition.animatedHorizontalShape(
    selected: Boolean,
    small: Dp = 8.dp,
    large: Dp = 24.dp
): RoundedCornerShape {
    val pairCornerValue = this.toHorizontalCornerValue(large = large, small = small)
    return animatedRoundedCornerShape(
        selected = selected,
        initial = pairCornerValue.first,
        animated = pairCornerValue.second
    )
}

@Composable
fun ItemPosition.animateVerticalShape(
    selected: Boolean,
    small: Dp = 8.dp,
    large: Dp = 24.dp
): RoundedCornerShape {
    val pairCornerValue = this.toVerticalCornerValue(large = large, small = small)
    return animatedRoundedCornerShape(
        selected = selected,
        initial = pairCornerValue.first,
        animated = pairCornerValue.second
    )
}

private fun ItemPosition.toVerticalCornerValue(
    large: Dp, small: Dp
): Pair<CornerValue, CornerValue> {
    val selectedCornerValue = CornerValue(
        topStart = large,
        topEnd = large,
        bottomStart = large,
        bottomEnd = large
    )

    val initial = when (this) {
        ItemPosition.TOP -> CornerValue(
            topStart = large,
            topEnd = large,
            bottomStart = small,
            bottomEnd = small
        )

        ItemPosition.MIDDLE -> CornerValue(
            topStart = small,
            topEnd = small,
            bottomStart = small,
            bottomEnd = small
        )

        ItemPosition.SINGLE -> CornerValue(
            topStart = large,
            topEnd = large,
            bottomStart = large,
            bottomEnd = large
        )

        ItemPosition.BOTTOM -> CornerValue(
            topStart = small,
            topEnd = small,
            bottomStart = large,
            bottomEnd = large
        )
    }

    return initial to selectedCornerValue
}

private fun ItemPosition.toHorizontalCornerValue(
    large: Dp,
    small: Dp
): Pair<CornerValue, CornerValue> {
    val selectedCornerValue = CornerValue(
        topStart = large,
        topEnd = large,
        bottomStart = large,
        bottomEnd = large
    )

    val initial = when (this) {
        ItemPosition.TOP -> CornerValue(
            topStart = large,
            topEnd = small,
            bottomStart = large,
            bottomEnd = small
        )

        ItemPosition.MIDDLE -> CornerValue(
            topStart = small,
            topEnd = small,
            bottomStart = small,
            bottomEnd = small
        )

        ItemPosition.SINGLE -> CornerValue(
            topStart = large,
            topEnd = large,
            bottomStart = large,
            bottomEnd = large
        )

        ItemPosition.BOTTOM -> CornerValue(
            topStart = small,
            topEnd = large,
            bottomStart = small,
            bottomEnd = large
        )
    }

    return initial to selectedCornerValue
}

@Immutable
data class CornerValue(
    val topStart: Dp,
    val topEnd: Dp,
    val bottomStart: Dp,
    val bottomEnd: Dp
)

@Composable
private fun animatedRoundedCornerShape(
    selected: Boolean,
    initial: CornerValue,
    animated: CornerValue
): RoundedCornerShape {
    val animatedTopStart by animateDpAsState(targetValue = if (selected) animated.topStart else initial.topStart)
    val animatedTopEnd by animateDpAsState(targetValue = if (selected) animated.topEnd else initial.topEnd)
    val animatedBottomStart by animateDpAsState(targetValue = if (selected) animated.bottomStart else initial.bottomStart)
    val animatedBottomEnd by animateDpAsState(targetValue = if (selected) animated.bottomEnd else initial.bottomEnd)

    return RoundedCornerShape(
        topStart = animatedTopStart,
        topEnd = animatedTopEnd,
        bottomStart = animatedBottomStart,
        bottomEnd = animatedBottomEnd
    )
}