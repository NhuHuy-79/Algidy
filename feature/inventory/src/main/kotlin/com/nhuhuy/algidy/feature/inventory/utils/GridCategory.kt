package com.nhuhuy.algidy.feature.inventory.utils

import androidx.annotation.StringRes
import com.nhuhuy.algidy.core.presentation.R

enum class GridCategory {
    ALL, FRIDGE, FREEZE, PANTRY, OTHER,
}


@StringRes
fun GridCategory.toStringRes(): Int {
    return when (this) {
        GridCategory.ALL -> R.string.category_add
        GridCategory.FRIDGE -> R.string.category_fridge
        GridCategory.FREEZE -> R.string.category_freezer
        GridCategory.PANTRY -> R.string.category_pantry
        GridCategory.OTHER -> R.string.category_other
    }
}
