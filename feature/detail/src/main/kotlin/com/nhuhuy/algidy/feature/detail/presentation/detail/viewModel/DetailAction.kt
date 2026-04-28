package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation

@Stable
sealed interface DetailAction {
    sealed interface EditEntryAction : DetailAction {
        data class OnNameChange(val name: String) : EditEntryAction
        data class OnQuantityChange(val quantity: Double) : EditEntryAction
        data class OnStorageLocationChange(val location: StorageLocation) : EditEntryAction
        data class OnExpiryDateChange(val expiryDate: Long) : EditEntryAction
        data class OnPurchaseDateChange(val purchaseDate: Long) : EditEntryAction
        data class OnNoteChange(val note: String) : EditEntryAction
        data class OnItemUnitChange(val unit: ItemUnit) : EditEntryAction
        data object OnSave : EditEntryAction
    }

    data object OnDismiss : DetailAction
    data object OnWastedItem : DetailAction
    data object OnConsumeItem : DetailAction
    data object OnEditItem : DetailAction
}