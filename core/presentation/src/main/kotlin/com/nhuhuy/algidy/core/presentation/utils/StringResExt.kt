package com.nhuhuy.algidy.core.presentation.utils

import androidx.annotation.StringRes
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.DAIRY_EGGS
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.FRUITS
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.MEAT
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.OTHERS
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.SEAFOOD
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.VEGETABLES
import com.nhuhuy.algidy.core.presentation.R

@StringRes
fun DefaultFoodCategory.toStringRes(): Int {
    return when (this) {
        VEGETABLES -> R.string.category_vegetables
        FRUITS -> R.string.category_fruits
        MEAT -> R.string.category_meat
        SEAFOOD -> R.string.category_seafood
        DAIRY_EGGS -> R.string.category_dairy_eggs
        OTHERS -> R.string.category_other
    }
}