package com.nhuhuy.algidy.core.model.food


enum class DefaultFoodCategory {
    VEGETABLES,
    FRUITS,
    MEAT,
    SEAFOOD,
    DAIRY_EGGS,
    OTHERS;

    companion object {
        fun fromName(name: String?): DefaultFoodCategory {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: OTHERS
        }
    }
}
