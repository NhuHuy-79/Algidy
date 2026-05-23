package com.nhuhuy.algidy.core.presentation.viewmodel

import android.net.Uri
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation

sealed interface FoodEntryAction {
    data class OnNameChange(val name: String) : FoodEntryAction
    data class OnQuantityChange(val quantity: Double) : FoodEntryAction
    data class OnStorageLocationChange(val location: StorageLocation) : FoodEntryAction
    data class OnExpiryDateChange(val expiryDate: Long) : FoodEntryAction
    data class OnPurchaseDateChange(val purchaseDate: Long) : FoodEntryAction
    data class OnNoteChange(val note: String) : FoodEntryAction
    data class OnItemUnitChange(val unit: ItemUnit) : FoodEntryAction
    data class OnImagePick(val uri: Uri) : FoodEntryAction
    data class OnCategoryChange(val categoryId: String) : FoodEntryAction
    data class OnCategoryQueryChange(val query: String) : FoodEntryAction
}
