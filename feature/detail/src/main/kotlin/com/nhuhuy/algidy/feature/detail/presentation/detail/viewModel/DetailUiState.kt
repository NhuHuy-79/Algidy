package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation
import com.nhuhuy.algidy.core.model.ValidationResult

@Immutable
data class DetailUiState(
    val foodItem: FoodItem = FoodItem(),
    val actionState: DetailActionState = DetailActionState.None
)

@Immutable
data class EditEntryUiState(
    val name: String = "",
    val categoryId: String = "",
    val location: StorageLocation = StorageLocation.FRIDGE,
    val quantity: Double = 0.0,
    val itemUnit: ItemUnit = ItemUnit.KG,
    val purchaseDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = -1,
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val notes: String = ""
)

@Immutable
data class EditEntryError(
    val nameError: ValidationResult = ValidationResult.SUCCESS,
    val quantityError: ValidationResult = ValidationResult.SUCCESS,
    val expiryDateError: ValidationResult = ValidationResult.SUCCESS
) {
    val allError: Boolean
        get() = nameError == ValidationResult.SUCCESS &&
                quantityError == ValidationResult.SUCCESS &&
                expiryDateError == ValidationResult.SUCCESS

}

enum class DetailActionState {
    None,
    Wasted,
    Consume,
    Edit,
}
