package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toUiModel
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetInventoryPreferenceUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.AddCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.DeleteCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.EditCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.DeleteFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.ObserveFoodItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteFoodItemUseCase: DeleteFoodItemUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    private val markFoodAsConsumedUseCase: MarkFoodAsConsumedUseCase,
    private val markFoodAsWastedUseCase: MarkFoodAsWastedUseCase,
    private val getInventoryPreferenceUseCase: GetInventoryPreferenceUseCase,
    private val navigator: Navigator,
    observerFoodItemUseCase: ObserveFoodItemUseCase,
    observeSettingDataUseCase: ObserveSettingDataUseCase,
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val appNewFeaturesReader: AppNewFeaturesReader
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>() {
    private val _uiState = MutableStateFlow(
        InventoryUiState(
            currentVersionCode = appNewFeaturesReader.currentVersionCode.toInt()
        )
    )
    override val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<InventoryCombineState> = combine(
        observeSettingDataUseCase.getCategoryEnabled(),
        observeCategoriesUseCase(),
        getInventoryPreferenceUseCase.observe(),
        observeSettingDataUseCase.getCameraPolicyAccepted()
    ) { categoryEnabled, categories, appVersion, cameraPolicyAccepted ->
        InventoryCombineState(
            categoryEnabled = categoryEnabled,
            categories = categories.toUiModel(),
            appVersionToNotify = appVersion,
            cameraPolicyAccepted = cameraPolicyAccepted
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InventoryCombineState()
    )


    val resultState: StateFlow<InventoryResultState> = observerFoodItemUseCase()
        .map { items ->
            if (items.isEmpty()) InventoryResultState.Empty
            else InventoryResultState.Success(items = items)
        }
        .onStart { emit(InventoryResultState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InventoryResultState.Loading
        )

    override fun onAction(action: InventoryAction) {
        when (action) {
            is InventoryAction.RemoveItem -> {
                viewModelScope.launch {
                    deleteFoodItemUseCase(id = action.id)
                }
            }

            InventoryAction.OnDismiss -> _uiState.product { copy(overlay = InventoryOverlay.None) }
            is InventoryAction.OnCategorySelect -> _uiState.product {
                copy(currentCategory = action.categoryUiModel)
            }

            is InventoryAction.OnCreateCategory -> {
                viewModelScope.launch {
                    addCategoryUseCase(action.name)
                }
            }

            is InventoryAction.OnEditCategorySheet.OnInputChange -> {
                _uiState.product { copy(categoryInput = action.value) }
            }

            InventoryAction.OnEditCategorySheet.Open -> {
                val currentCategory = currentState.currentCategory
                if (currentCategory is CategoryUiModel.ByCategory) {
                    _uiState.product {
                        copy(
                            overlay = InventoryOverlay.CategoryEdit,
                            categoryInput = currentCategory.data.name
                        )
                    }
                }
            }

            InventoryAction.OnEditCategorySheet.Save -> {
                viewModelScope.launch {
                    val category = currentState.currentCategory
                    val text = currentState.categoryInput
                    if (category is CategoryUiModel.ByCategory) {
                        val newCategory = category.data.copy(name = text)
                        editCategoryUseCase(category = newCategory)
                        _uiState.product { copy(overlay = InventoryOverlay.None) }
                    }
                }
            }

            InventoryAction.OnDeleteAlertConfirm -> {
                viewModelScope.launch {
                    val category = currentState.currentCategory
                    if (category is CategoryUiModel.ByCategory) {
                        deleteCategoryUseCase(category.data.id)
                        _uiState.product { copy(overlay = InventoryOverlay.None) }
                    }
                }
            }

            InventoryAction.OnConsumeConfirm -> {
                viewModelScope.launch {
                    val ids = if (currentState.isSelectMode) currentState.selectedFoodIds.toList()
                    else listOf(currentState.currentFoodItem.id)

                    _uiState.product {
                        copy(
                            overlay = InventoryOverlay.None,
                            selectedFoodIds = emptySet()
                        )
                    }
                    markFoodAsConsumedUseCase.executeWithList(foodIds = ids)
                }
            }

            InventoryAction.OnWasteConfirm -> {
                viewModelScope.launch {
                    val ids = if (currentState.isSelectMode) currentState.selectedFoodIds.toList()
                    else listOf(currentState.currentFoodItem.id)

                    _uiState.product {
                        copy(
                            overlay = InventoryOverlay.None,
                            selectedFoodIds = emptySet()
                        )
                    }
                    markFoodAsWastedUseCase.executeWithList(foodIds = ids)
                }
            }

            InventoryAction.OnDeleteCategory -> {
                _uiState.product { copy(overlay = InventoryOverlay.CategoryDelete) }
            }

            InventoryAction.OnSearchClick -> {
                navigator.navigateTo(Destination.Inventory.Search)
            }

            is InventoryAction.OnItemClick -> {
                _uiState.product {
                    copy(
                        currentFoodItem = action.item,
                        overlay = InventoryOverlay.ItemDetail
                    )
                }
            }

            InventoryAction.OnResetFilters -> {
                _uiState.product {
                    copy(sortMode = InventorySortMode.NONE, showExpiredOnly = false)
                }
            }

            InventoryAction.OnShowExpiredOnly -> {
                _uiState.product {
                    copy(showExpiredOnly = !showExpiredOnly)
                }
            }

            InventoryAction.OnSortByExpiry -> {
                _uiState.product {
                    copy(sortMode = InventorySortMode.BY_EXPIRY)
                }
            }

            InventoryAction.OnSortByName -> {
                _uiState.product {
                    copy(sortMode = InventorySortMode.BY_NAME)
                }
            }

            InventoryAction.OnConfirmCameraPolicy -> {
                viewModelScope.launch {
                    getInventoryPreferenceUseCase.setCameraPolicyAccepted(true)
                    _uiState.product { copy(overlay = InventoryOverlay.None) }
                    emitEvent(InventoryEvent.RequestCameraPermission)
                }
            }

            is InventoryFabAction -> onFabAction(action)
            is InventoryDetailAction -> onDetailAction(action)
            is InventoryAction.OnAddCategory.OnInputChange -> {
                _uiState.product {
                    copy(categoryInput = action.value)
                }
            }

            InventoryAction.OnAddCategory.Open -> {
                _uiState.product {
                    copy(overlay = InventoryOverlay.CategoryAdd)
                }
            }

            InventoryAction.OnAddCategory.Save -> {
                _uiState.product {
                    copy(overlay = InventoryOverlay.None)
                }
                viewModelScope.launch {
                    addCategoryUseCase(currentState.categoryInput)
                }
            }

            is InventoryAction.OnCameraPermissionAccept -> navigator.navigateTo(Destination.Scanner)

            is InventorySelectAction -> onSelectAction(action)
            InventoryAction.ShowAppFeature -> viewModelScope.launch {
                val newFeature = appNewFeaturesReader.getWhatsNewContent()
                newFeature?.let {
                    _uiState.product {
                        copy(overlay = InventoryOverlay.NewFeatureSheet(it))
                    }
                    getInventoryPreferenceUseCase.setVersion(it.versionCode)
                }
            }
        }
    }

    private fun onDetailAction(action: InventoryDetailAction) {
        when (action) {
            InventoryDetailAction.OnConsumedClick -> _uiState.product {
                copy(overlay = InventoryOverlay.ConsumeConfirm)
            }

            InventoryDetailAction.OnEditClick -> viewModelScope.launch {
                _uiState.product { copy(overlay = InventoryOverlay.None) }
                navigator.navigateTo(Destination.FoodEntry(initialFoodItem = currentState.currentFoodItem))
            }

            InventoryDetailAction.OnWastedClick -> _uiState.product {
                copy(overlay = InventoryOverlay.WasteConfirm)
            }

            InventoryDetailAction.Open -> _uiState.product {
                copy(overlay = InventoryOverlay.ItemDetail)
            }
        }
    }

    private fun onSelectAction(action: InventorySelectAction) {
        val selectedFoodIds = currentState.selectedFoodIds
        when (action) {
            InventorySelectAction.ClearSelection -> {
                _uiState.product {
                    copy(selectedFoodIds = emptySet())
                }
            }

            InventorySelectAction.ConsumeAll -> _uiState.product {
                copy(overlay = InventoryOverlay.ConsumeConfirm)
            }

            is InventorySelectAction.OnClick -> {
                if (action.id in selectedFoodIds) {
                    _uiState.product {
                        copy(selectedFoodIds = selectedFoodIds - action.id)
                    }
                } else {
                    _uiState.product {
                        copy(selectedFoodIds = selectedFoodIds + action.id)
                    }
                }
            }

            is InventorySelectAction.OnLongClick -> {
                if (action.id in selectedFoodIds) {
                    _uiState.product {
                        copy(selectedFoodIds = selectedFoodIds - action.id)
                    }
                } else {
                    _uiState.product {
                        copy(selectedFoodIds = selectedFoodIds + action.id)
                    }
                }
            }

            InventorySelectAction.SelectAll -> {
                val allFoods = when (val foodState = resultState.value) {
                    is InventoryResultState.Success -> foodState.items
                    else -> emptyList()
                }

                if (allFoods.isNotEmpty()) {
                    _uiState.product {
                        copy(selectedFoodIds = allFoods.map { it.id }.toSet())
                    }
                }
            }

            InventorySelectAction.WasteAll -> _uiState.product {
                copy(overlay = InventoryOverlay.WasteConfirm)
            }
        }
    }

    private fun onFabAction(action: InventoryFabAction) {
        when (action) {
            InventoryFabAction.Analytics -> {
                _uiState.product { copy(expanded = false) }
                navigator.navigateTo(Destination.Analytics)
            }

            is InventoryFabAction.BarcodeScan -> {
                if (action.isPermissionGranted) {
                    _uiState.product { copy(expanded = false) }
                    navigator.navigateTo(Destination.Scanner)
                } else if (combineState.value.cameraPolicyAccepted) {
                    _uiState.product { copy(expanded = false) }
                    emitEvent(InventoryEvent.RequestCameraPermission)
                } else {
                    _uiState.product {
                        copy(
                            expanded = false,
                            overlay = InventoryOverlay.CameraPolicySheet
                        )
                    }
                }
            }

            InventoryFabAction.Manual -> {
                _uiState.product { copy(expanded = false) }
                navigator.navigateTo(Destination.FoodEntry())
            }

            InventoryFabAction.Setting -> {
                _uiState.product { copy(expanded = false) }
                navigator.navigateTo(Destination.Setting(SettingDestination.Main))
            }

            is InventoryFabAction.ToggleFabMenu -> {
                _uiState.product { copy(expanded = action.value) }
            }
        }
    }
}
