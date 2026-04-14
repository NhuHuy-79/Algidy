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
    val nameValidation: ValidationResult = ValidationResult.IDLE,
    val quantityValidation: ValidationResult = ValidationResult.IDLE,
    val expiryDateValidation: ValidationResult = ValidationResult.IDLE
) {
    val isNameError: Boolean
        get() = nameValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val isQuantityError: Boolean
        get() = quantityValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val isExpiryDateError: Boolean
        get() = expiryDateValidation !in listOf(
            ValidationResult.SUCCESS,
            ValidationResult.IDLE
        )
    val valid: Boolean
        get() = nameValidation == ValidationResult.SUCCESS &&
                quantityValidation == ValidationResult.SUCCESS &&
                expiryDateValidation == ValidationResult.SUCCESS

}

enum class DetailActionState {
    None,
    Wasted,
    Consume,
    Edit,
}
