package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.domain.repository.CategoryRepository
import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.presentation.deeplink.DeepLinkStore
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toUiModel
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetInventoryPreferenceUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay.AddFoodBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay.EditFoodSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay.ItemDetail
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay.NewFeatureSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay.None
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.model.toFoodUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

internal class InventoryViewModel(
    observeSettingDataUseCase: ObserveSettingDataUseCase,
    private val foodRepository: FoodRepository,
    private val categoryRepository: CategoryRepository,
    private val getInventoryPreferenceUseCase: GetInventoryPreferenceUseCase,
    private val navigator: Navigator,
    private val appNewFeaturesReader: AppNewFeaturesReader,
    private val deepLinkStore: DeepLinkStore
) : BaseViewModel<InventoryUiState, InventoryEvent, InventoryAction>() {
    private val _uiState = MutableStateFlow(
        InventoryUiState(currentVersionCode = appNewFeaturesReader.currentVersionCode.toInt())
    )
    override val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<InventoryCombineState> = combine(
        observeSettingDataUseCase.getCategoryEnabled(),
        categoryRepository.observeAllCategories(),
        getInventoryPreferenceUseCase.observe(),
    ) { categoryEnabled, categories, generaPreferences ->
        InventoryCombineState(
            categoryEnabled = categoryEnabled,
            categories = categories.toUiModel(),
            generalPreferences = generaPreferences,
            isLoaded = true
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InventoryCombineState()
    )

    private val currentCombineState get() = combineState.value

    val resultState: StateFlow<InventoryResultState> = foodRepository.observeAllActiveFoodItems()
        .map { items ->
            if (items.isEmpty()) InventoryResultState.Empty
            else InventoryResultState.Success(items = items.toFoodUiModel())
        }
        .onStart { emit(InventoryResultState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InventoryResultState.Loading
        )

    init {
        Timber.d("InventoryViewModel init ${hashCode()}")
        deepLinkStore.foodId.onEach { foodId ->
            val currentFoodUiModel =
                foodRepository.getFoodById(foodId)?.toFoodUiModel() ?: FoodUiModel()
            _uiState.product {
                copy(deepLinkFoodDetailId = foodId, currentFoodItem = currentFoodUiModel)
            }
        }.launchIn(viewModelScope)
    }

    override fun onAction(action: InventoryAction) {
        when (action) {

            InventoryAction.OnDismiss -> _uiState.product { copy(overlay = None) }

            InventoryAction.OnSearchClick -> {
                navigator.navigateTo(Destination.Inventory.Search)
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
                _uiState.product { copy(overlay = None) }
                emitEvent(InventoryEvent.RequestCameraPermission)
                viewModelScope.launch {
                    getInventoryPreferenceUseCase.updatePreferences(
                        generalPreferences = currentCombineState.generalPreferences.copy(
                            isCameraPolicyAccepted = true
                        )
                    )
                }
            }

            is InventoryFabAction -> onFabAction(action)

            is InventoryAction.OnCameraPermissionAccept -> {
                navigator.navigateTo(Destination.Scanner)
            }

            is InventorySelectAction -> onSelectAction(action)
            InventoryAction.ShowAppFeature -> viewModelScope.launch {
                val newFeature = appNewFeaturesReader.getWhatsNewContent()

                newFeature?.let { versionFeatures ->
                    _uiState.product {
                        copy(overlay = NewFeatureSheet(versionFeatures))
                    }
                }

                getInventoryPreferenceUseCase.updatePreferences(
                    generalPreferences = currentCombineState.generalPreferences.copy(
                        appVersionToNotify = currentState.currentVersionCode
                    )
                )
            }

            InventoryAction.OnEmptyPageClick -> {
                _uiState.product {
                    copy(overlay = AddFoodBottomSheet())
                }
            }

            InventoryAction.ShowDeepLinkResult -> {
                val foodId = currentState.deepLinkFoodDetailId ?: return
                viewModelScope.launch {
                    val currentFoodUiModel = foodRepository.getFoodById(foodId)?.toFoodUiModel()
                    currentFoodUiModel?.let { foodUiModel ->
                        Timber.d("Show Detail Food")
                        _uiState.product {
                            copy(
                                overlay = ItemDetail,
                                currentFoodItem = foodUiModel

                            )
                        }
                    }
                }
            }

            is InventoryAction.ShowOverlay -> {
                _uiState.product { copy(overlay = action.overlay) }
            }

            is InventoryCategoryAction -> onCategoryAction(action)
            is InventoryFoodAction -> onFoodAction(action)
        }
    }

    private fun onFoodAction(action: InventoryFoodAction) {
        when (action) {
            is InventoryFoodAction.Click -> {
                _uiState.product {
                    copy(currentFoodItem = action.food, overlay = ItemDetail)
                }
            }

            InventoryFoodAction.Consume -> {
                viewModelScope.launch {
                    val ids = if (currentState.isSelectMode) currentState.selectedFoodIds.toList()
                    else listOf(currentState.currentFoodItem.id)

                    _uiState.product {
                        copy(
                            overlay = None,
                            selectedFoodIds = emptySet()
                        )
                    }
                    foodRepository.updateFoodStatusList(ids = ids, newStatus = FoodStatus.CONSUMED)
                }
            }

            is InventoryFoodAction.DeleteFood -> viewModelScope.launch {
                foodRepository.deleteFoodById(action.id)
            }

            InventoryFoodAction.Waste -> {
                viewModelScope.launch {
                    val ids = if (currentState.isSelectMode) currentState.selectedFoodIds.toList()
                    else listOf(currentState.currentFoodItem.id)

                    _uiState.product {
                        copy(
                            overlay = None,
                            selectedFoodIds = emptySet()
                        )
                    }
                    foodRepository.updateFoodStatusList(ids = ids, newStatus = FoodStatus.WASTED)
                }
            }

            is InventoryFoodAction.Edit -> _uiState.product {
                copy(overlay = EditFoodSheet(action.food))
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

    private fun onCategoryAction(action: InventoryCategoryAction) {
        when (action) {
            is InventoryCategoryAction.Select -> {
                _uiState.product {
                    copy(currentCategory = action.categoryUiModel)
                }
            }

            InventoryCategoryAction.AddCategory -> {
                _uiState.product {
                    copy(overlay = None)
                }
                viewModelScope.launch {
                    val newCategory = FoodCategory(name = currentState.categoryInput)
                    categoryRepository.addCategory(newCategory)
                }
            }

            InventoryCategoryAction.Delete -> {
                viewModelScope.launch {
                    val category = currentState.currentCategory
                    if (category is CategoryUiModel.ByCategory) {
                        categoryRepository.deleteCategory(category.data.id)
                        _uiState.product { copy(overlay = None) }
                    }
                }
            }

            InventoryCategoryAction.EditCategory -> {
                viewModelScope.launch {
                    val category = currentState.currentCategory
                    val text = currentState.categoryInput
                    if (category is CategoryUiModel.ByCategory) {
                        val newCategory = category.data.copy(name = text)
                        categoryRepository.updateCategory(category = newCategory)
                        _uiState.product { copy(overlay = None) }
                    }
                }
            }

            is InventoryCategoryAction.InputChange -> {
                _uiState.product {
                    copy(categoryInput = action.input)
                }
            }

            InventoryCategoryAction.OpenCreate -> {
                _uiState.product {
                    copy(overlay = InventoryOverlay.CategoryAdd)
                }
            }

            InventoryCategoryAction.OpenEdit -> {
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
        }
    }

    private fun onFabAction(action: InventoryFabAction) {
        when (action) {
            InventoryFabAction.Analytics -> {
                navigator.navigateTo(Destination.Analytics)
            }

            is InventoryFabAction.BarcodeScan -> {
                if (action.isPermissionGranted) {
                    emitEvent(InventoryEvent.NavigateToScanner)
                } else if (combineState.value.generalPreferences.isCameraPolicyAccepted) {
                    emitEvent(InventoryEvent.RequestCameraPermission)
                } else {
                    _uiState.product { copy(overlay = InventoryOverlay.CameraPolicySheet) }
                }
            }

            InventoryFabAction.Manual -> {
                _uiState.product {
                    copy(overlay = AddFoodBottomSheet())
                }
            }

            InventoryFabAction.Setting -> {
                navigator.navigateTo(Destination.Setting(SettingDestination.Main))
            }

            is InventoryFabAction.OnChangeFabVisibility -> {
                _uiState.product { copy(visibility = action.value) }
            }
        }
    }

    override fun onCleared() {
        Timber.d("InventoryViewModel cleared ${hashCode()}")
        super.onCleared()
    }
}
