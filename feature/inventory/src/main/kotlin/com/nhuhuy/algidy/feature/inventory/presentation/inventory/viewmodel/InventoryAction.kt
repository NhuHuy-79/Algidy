package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

@Stable
sealed interface InventoryAction : UiAction {
    data class ShowOverlay(val overlay: InventoryOverlay) : InventoryAction
    data object ShowAppFeature : InventoryAction
    data object ShowDeepLinkResult : InventoryAction
    data object OnDismiss : InventoryAction
    data object OnSearchClick : InventoryAction
    data object OnResetFilters : InventoryAction
    data object OnSortByExpiry : InventoryAction
    data object OnSortByName : InventoryAction
    data object OnShowExpiredOnly : InventoryAction
    data object OnConfirmCameraPolicy : InventoryAction
    data object OnCameraPermissionAccept : InventoryAction
    data object OnEmptyPageClick : InventoryAction
}

@Stable
sealed interface InventorySelectAction : InventoryAction {
    data class OnClick(val id: String) : InventorySelectAction
    data class OnLongClick(val id: String) : InventorySelectAction
    data object SelectAll : InventorySelectAction
    data object ClearSelection : InventorySelectAction
    data object ConsumeAll : InventorySelectAction
    data object WasteAll : InventorySelectAction
}

@Stable
sealed interface InventoryFabAction : InventoryAction {
    data class OnChangeFabVisibility(val value: Boolean) : InventoryFabAction
    data object Manual : InventoryFabAction
    data object Analytics : InventoryFabAction
    data object Setting : InventoryFabAction
    data class BarcodeScan(val isPermissionGranted: Boolean) : InventoryFabAction
}