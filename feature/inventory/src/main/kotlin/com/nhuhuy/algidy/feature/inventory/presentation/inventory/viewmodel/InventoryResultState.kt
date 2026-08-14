package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import com.nhuhuy.algidy.core.model.VersionFeatures
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Stable
sealed interface InventoryResultState {
    object Loading : InventoryResultState
    data class Success(val items: List<FoodItem>) : InventoryResultState
    object Empty : InventoryResultState
}

@Immutable
data class InventoryCombineState(
    val categoryEnabled: Boolean = false,
    val categories: List<CategoryUiModel> = listOf(CategoryUiModel.All),
    val generalPreferences: GeneralPreferences = GeneralPreferences(),
    val isLoaded: Boolean = false
)

@Immutable
data class InventoryUiState(
    val currentVersionCode: Int = 1,
    val expanded: Boolean = false,
    val currentCategory: CategoryUiModel = CategoryUiModel.All,
    val currentFoodItem: FoodItem = FoodItem(),
    val categoryInput: String = "",
    val overlay: InventoryOverlay = InventoryOverlay.None,
    val sortMode: InventorySortMode = InventorySortMode.NONE,
    val selectedFoodIds: Set<String> = emptySet(),
    val showExpiredOnly: Boolean = false
) : UiState {
    val isSelectMode: Boolean get() = selectedFoodIds.isNotEmpty()
    val showCategoryEdit: Boolean get() = currentCategory is CategoryUiModel.ByCategory
}

sealed interface InventoryOverlay {
    data object None : InventoryOverlay
    data object CategoryEdit : InventoryOverlay
    data object CategoryDelete : InventoryOverlay
    data object ItemDetail : InventoryOverlay

    data object CategoryAdd : InventoryOverlay
    data object ConsumeConfirm : InventoryOverlay
    data object WasteConfirm : InventoryOverlay
    data class NewFeatureSheet(val versionFeature: VersionFeatures) : InventoryOverlay
    data object CameraPolicySheet : InventoryOverlay
}
enum class InventorySortMode {
    BY_NAME, BY_EXPIRY, NONE,
}
