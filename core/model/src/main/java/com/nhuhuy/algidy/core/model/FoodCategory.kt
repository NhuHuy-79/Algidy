package com.nhuhuy.algidy.core.model

enum class FoodCategory {
    VEGETABLES,
    FRUITS,
    MEAT,
    SEAFOOD,
    DAIRY_EGGS,
    OTHERS;

    companion object {
        fun fromName(name: String?): FoodCategory {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: OTHERS
        }
    }
}