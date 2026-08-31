package com.nhuhuy.algidy.feature.inventory.presentation.preview

import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.ImageProvider
import com.nhuhuy.algidy.feature.inventory.R
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryCombineState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiState
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface
import kotlinx.collections.immutable.persistentListOf

object FakeData {
    val fakeSearchUiState = SearchUiState(
        query = "",
        searchHistories = persistentListOf(
            SearchHistory(
                id = 1,
                name = "Milk",
                timeStamp = System.currentTimeMillis(),
            ),
            SearchHistory(
                id = 2,
                name = "Bread",
                timeStamp = System.currentTimeMillis() - 60_000,
            ),
            SearchHistory(
                id = 3,
                name = "Yogurt",
                timeStamp = System.currentTimeMillis() - 3_600_000,
            ),
        ),
        surface = SearchUiSurface.None,
        currentCategory = CategoryUiModel.All,
        categories = persistentListOf(
            CategoryUiModel.All,
            CategoryUiModel.Uncategorized,
        ),
    )
    val combineState = InventoryCombineState()
    val uiState = InventoryUiState(
        currentVersionCode = 7,
    )
    val items = listOf(
        FoodUiModel(
            id = "1",
            imageProvider = ImageProvider.Drawable(R.drawable.img_milk),
            name = "Fresh Milk",
            remainDays = 3,
            freshness = Freshness.URGENT,
            location = StorageLocation.FRIDGE,
        ),

        FoodUiModel(
            id = "2",
            imageProvider = ImageProvider.Drawable(R.drawable.img_apple),
            name = "Red Apples",
            remainDays = 8,
            freshness = Freshness.WARNING,
            location = StorageLocation.FRIDGE,
        ),

        FoodUiModel(
            id = "3",
            imageProvider = ImageProvider.Drawable(R.drawable.img_chicken),
            name = "Chicken Breast",
            remainDays = 1,
            freshness = Freshness.URGENT,
            location = StorageLocation.FREEZER,
        ),

        FoodUiModel(
            id = "4",
            imageProvider = ImageProvider.Drawable(R.drawable.img_yogurt),
            name = "Greek Yogurt",
            remainDays = 14,
            freshness = Freshness.FRESH,
            location = StorageLocation.FRIDGE,
        ),

        FoodUiModel(
            id = "5",
            imageProvider = ImageProvider.Drawable(R.drawable.img_bread),
            name = "Whole Wheat Bread",
            remainDays = -2,
            freshness = Freshness.EXPIRED,
            location = StorageLocation.PANTRY,
        ),

        FoodUiModel(
            id = "6",
            imageProvider = ImageProvider.Drawable(R.drawable.img_tomato),
            name = "Tomatoes",
            remainDays = 6,
            freshness = Freshness.WARNING,
            location = StorageLocation.PANTRY,
        ),
    )
    val resultState = InventoryResultState.Success(
        items = items
    )
}