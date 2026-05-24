package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.AddCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.DeleteCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.EditCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.DeleteFoodItemUseCase
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
    private val observerFoodItemUseCase: ObserveFoodItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteFoodItemUseCase: DeleteFoodItemUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    private val observeSettingDataUseCase: ObserveSettingDataUseCase,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>() {
    private val _uiState = MutableStateFlow(InventoryUiState())
    override val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<InventoryCombineState> = combine(
        observeSettingDataUseCase.getCategoryEnabled(),
        observeCategoriesUseCase()
    ) { categoryEnabled, categories ->
        InventoryCombineState(
            categoryEnabled = categoryEnabled,
            categories = categories.map { CategoryUiModel.ByCategory(it) }
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
            InventoryAction.OnDismiss -> _uiState.product { copy(overlay = InventoryOverlay.NONE) }
            is InventoryAction.OnCategorySelect -> _uiState.product {
                copy(currentCategory = action.categoryUiModel)
            }

            is InventoryAction.OnCreateCategory -> {
                viewModelScope.launch {
                    addCategoryUseCase(action.name)
                }
            }

            is InventoryAction.OnEditCategorySheet.OnInputChange -> {
                _uiState.product { copy(categorySheetInput = action.value) }
            }

            InventoryAction.OnEditCategorySheet.Open -> {
                val currentCategory = currentState.currentCategory
                if (currentCategory is CategoryUiModel.ByCategory) {
                    _uiState.product {
                        copy(
                            overlay = InventoryOverlay.CATEGORY_EDIT,
                            categorySheetInput = currentCategory.data.name
                        )
                    }
                }
            }

            InventoryAction.OnEditCategorySheet.Save -> {
                viewModelScope.launch {
                    val category = currentState.currentCategory
                    val text = currentState.categorySheetInput
                    if (category is CategoryUiModel.ByCategory) {
                        val newCategory = category.data.copy(name = text)
                        editCategoryUseCase(category = newCategory)
                        _uiState.product { copy(overlay = InventoryOverlay.NONE) }
                    }
                }
            }

            InventoryAction.OnDeleteAlertConfirm -> {
                viewModelScope.launch {
                    val category = currentState.currentCategory
                    if (category is CategoryUiModel.ByCategory) {
                        deleteCategoryUseCase(category.data.id)
                        _uiState.product { copy(overlay = InventoryOverlay.NONE) }
                    }
                }
            }

            InventoryAction.OnDeleteCategory -> {
                _uiState.product { copy(overlay = InventoryOverlay.CATEGORY_DELETE) }
            }

            InventoryAction.OnSearchClick -> {
                emitEvent(InventoryEvent.NavigateToSearch)
            }

            is InventoryAction.OnItemClick -> {
                emitEvent(InventoryEvent.NavigateToDetail(action.id))
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

            is InventoryFabAction -> onFabAction(action)
        }
    }

    private fun onFabAction(action: InventoryFabAction) {
        _uiState.product { copy(expanded = !expanded) }
        when (action) {
            InventoryFabAction.Analytics -> emitEvent(InventoryEvent.NavigateToAnalytics)
            InventoryFabAction.BarcodeScan -> emitEvent(InventoryEvent.NavigateToCamera)
            InventoryFabAction.Manual -> emitEvent(InventoryEvent.NavigateToFoodEntry)
            InventoryFabAction.Setting -> emitEvent(InventoryEvent.NavigateToSetting)
            is InventoryFabAction.ToggleFabMenu -> Unit
        }
    }
}
