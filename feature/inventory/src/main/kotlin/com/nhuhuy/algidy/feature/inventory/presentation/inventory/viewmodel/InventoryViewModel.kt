package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegate
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegateImpl
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel.*
import com.nhuhuy.algidy.core.presentation.model.toUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction.*
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.AddCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.DeleteCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.EditCategoryUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.category.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.CreateFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.DeleteFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.ObserveFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryEvent.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Inventory screen.
 * Handles food items, categories, and filtering/sorting logic.
 */
class InventoryViewModel(
    observerFoodItemUseCase: ObserveFoodItemUseCase,
    private val foodEntryDelegateImpl: FoodEntryDelegateImpl,
    private val createFoodItemUseCase: CreateFoodItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteFoodItemUseCase: DeleteFoodItemUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    observeSettingDataUseCase: ObserveSettingDataUseCase,
    observeCategoriesUseCase: ObserveCategoriesUseCase
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>(),
    FoodEntryDelegate by foodEntryDelegateImpl {
    private val _uiState = MutableStateFlow(InventoryUiState())
    override val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<InventoryCombineState> = combine(
        observeSettingDataUseCase.getCategoryEnabled(),
        observeCategoriesUseCase()
    ) { categoryEnabled, categories ->
        InventoryCombineState(
            categoryEnabled = categoryEnabled,
            categories = categories.toUiModel()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InventoryCombineState()
    )

    init {
        resetEntryData()
    }

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

    override fun onEntryAction(action: FoodEntryAction) {
        when (action) {
            FoodEntryAction.OnCategoryConfirm -> {
                viewModelScope.launch {
                    val query = entryState.value.categoryQuery
                    if (query.isNotBlank()) {
                        val newCategory = addCategoryUseCase(query)
                        // Trigger standard select action in delegate
                        foodEntryDelegateImpl.updateCategories(
                            categories = entryState.value.categories + CategoryUiModel.ByCategory(data = newCategory)
                        )
                        foodEntryDelegateImpl.onEntryAction(
                            FoodEntryAction.OnCategorySelect(CategoryUiModel.ByCategory(newCategory))
                        )

                    }
                }
            }
            else -> foodEntryDelegateImpl.onEntryAction(action)
        }
    }

    override fun onAction(action: InventoryAction) {
        when (action) {
            is InventoryAction.RemoveItem -> {
                viewModelScope.launch {
                    deleteFoodItemUseCase(id = action.id)
                }
            }

            InventoryAction.OnAddFabClick -> _uiState.product {
                updateCategories(
                    categories = combineState.value.categories.filterIsInstance<CategoryUiModel.ByCategory>()
                )
                copy(overlay = InventoryOverlay.FOOD_SHEET)
            }

            InventoryAction.OnDismiss -> _uiState.product { copy(overlay = InventoryOverlay.NONE) }
            
            InventoryAction.OnManuallyClick -> viewModelScope.launch {
                val foodEntry: FoodItem = getResultFoodItem()
                _uiState.product { copy(overlay = InventoryOverlay.NONE) }
                resetEntryData()
                createFoodItemUseCase(foodItem = foodEntry)
            }

            is InventoryAction.ToggleFabMenu -> {
                _uiState.product { copy(expanded = action.value) }
            }

            is InventoryAction.OnCategorySelect -> _uiState.product {
                copy(currentCategory = action.categoryUiModel)
            }

            is InventoryAction.OnCreateCategory -> {
                viewModelScope.launch {
                    val newCategory = addCategoryUseCase(action.name)
                    onEntryAction(OnCategorySelect(ByCategory(newCategory)))
                }
            }

            is InventoryAction.OnEditCategorySheet.OnInputChange -> {
                _uiState.product {
                    copy(categorySheetInput = action.value)
                }
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

            InventoryAction.OnEditCategorySheet.Save -> viewModelScope.launch {
                val category = currentState.currentCategory
                val text = currentState.categorySheetInput

                if (category is CategoryUiModel.ByCategory) {
                    val newCategory = category.data.copy(name = text)
                    editCategoryUseCase(category = newCategory)
                    _uiState.product { copy(overlay = InventoryOverlay.NONE) }
                }
            }

            InventoryAction.OnDeleteCategory -> {
                _uiState.product {
                    copy(overlay = InventoryOverlay.CATEGORY_DELETE)
                }
            }

            is InventoryAction.OnItemClick -> {
                viewModelScope.launch {
                    emitEvent(NavigateToDetail(action.id))
                }
            }

            InventoryAction.OnSearchClick -> {
                viewModelScope.launch {
                    emitEvent(InventoryEvent.NavigateToSearch)
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

            InventoryAction.OnDeleteAlertConfirm -> viewModelScope.launch {
                val category = currentState.currentCategory
                if (category is CategoryUiModel.ByCategory) {
                    deleteCategoryUseCase(category.data.id)
                    _uiState.product { copy(currentCategory = CategoryUiModel.All) }
                }
            }
        }
    }
}
