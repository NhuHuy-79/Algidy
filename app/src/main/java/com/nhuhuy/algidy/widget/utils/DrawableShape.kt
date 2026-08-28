package com.nhuhuy.algidy.widget.utils

import androidx.annotation.DrawableRes
import com.nhuhuy.algidy.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition

@DrawableRes
fun ItemPosition.toVerticalDrawable(): Int {
    return when (this) {
        ItemPosition.TOP -> R.drawable.item_start_shape
        ItemPosition.MIDDLE -> R.drawable.item_middle_shape
        ItemPosition.SINGLE -> R.drawable.item_single_shape
        ItemPosition.BOTTOM -> R.drawable.item_bottom_shape
    }
}