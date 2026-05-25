package com.nhuhuy.algidy.core.presentation.utils

import androidx.annotation.StringRes
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory.*
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R

@StringRes
fun DefaultFoodCategory.toStringRes(): Int {
    return when (this) {
        VEGETABLES -> R.string.category_vegetables
        FRUITS -> R.string.category_fruits
        MEAT -> R.string.category_meat
        SEAFOOD -> R.string.category_seafood
        DAIRY_EGGS -> R.string.category_dairy_eggs
        OTHERS -> R.string.category_others
    }
}

@StringRes
fun Freshness.toStringRes(): Int {
    return when (this) {
        Freshness.EXPIRED -> R.string.freshness_status_expired
        Freshness.URGENT -> R.string.freshness_status_urgent
        Freshness.WARNING -> R.string.freshness_status_warning
        Freshness.FRESH -> R.string.freshness_status_fresh
    }
}

@StringRes
fun StorageLocation.toStringRes(): Int {
    return when (this) {
        StorageLocation.FRIDGE -> R.string.location_fridge
        StorageLocation.FREEZER -> R.string.location_freezer
        StorageLocation.PANTRY -> R.string.location_pantry
        StorageLocation.OTHER -> R.string.location_other
    }
}

@StringRes
fun ItemUnit.toStringRes(): Int {
    return when (this) {
        ItemUnit.KG -> R.string.unit_kg
        ItemUnit.GRAM -> R.string.unit_gram
        ItemUnit.LITER -> R.string.unit_liter
        ItemUnit.PIECE -> R.string.unit_piece
        ItemUnit.BOTTLE -> R.string.unit_bottle
        ItemUnit.OTHER -> R.string.unit_other
    }
}
