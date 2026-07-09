package com.nhuhuy.algidy.widget.state

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.StorageLocation

data class FoodWidgetModel(
    val id: String = "",
    val name: String = "",
    val storageLocation: StorageLocation = StorageLocation.OTHER,
)

fun List<FoodItem>.toFoodWidgetModelList(): List<FoodWidgetModel> {
    return map { foodItem ->
        FoodWidgetModel(
            id = foodItem.id,
            name = foodItem.name,
            storageLocation = foodItem.location
        )
    }
}