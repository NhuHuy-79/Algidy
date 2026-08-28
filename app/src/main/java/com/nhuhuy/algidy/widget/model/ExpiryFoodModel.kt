package com.nhuhuy.algidy.widget.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.StorageLocation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ExpiryFoodModel(
    val id: String = "",
    val name: String = "",
    val storageLocation: StorageLocation = StorageLocation.OTHER,
    val categoryName: String = ""
)

fun List<FoodItem>.toFoodWidgetModelList(): ImmutableList<ExpiryFoodModel> {
    return map { foodItem ->
        ExpiryFoodModel(
            id = foodItem.id,
            name = foodItem.name,
            storageLocation = foodItem.location,
            categoryName = foodItem.category?.name.orEmpty()
        )
    }.toImmutableList()
}

val fakeExpiryFoodList: ImmutableList<ExpiryFoodModel> = persistentListOf(
    ExpiryFoodModel(
        id = "1",
        name = "Strawberries",
        storageLocation = StorageLocation.FREEZER,
        categoryName = "Fruits"
    ),
    ExpiryFoodModel(
        id = "2",
        name = "Organic Greek Yogurt",
        storageLocation = StorageLocation.FRIDGE,
        categoryName = "Dairy"
    ),
    ExpiryFoodModel(
        id = "3",
        name = "Chicken Breast",
        storageLocation = StorageLocation.FREEZER,
        categoryName = "Meat"
    ),
    ExpiryFoodModel(
        id = "4",
        name = "Fresh Spinach",
        storageLocation = StorageLocation.PANTRY,
        categoryName = "Vegetables"
    ),
    ExpiryFoodModel(
        id = "5",
        name = "Sourdough Bread",
        storageLocation = StorageLocation.OTHER,
        categoryName = "Bakery"
    )
).toImmutableList()