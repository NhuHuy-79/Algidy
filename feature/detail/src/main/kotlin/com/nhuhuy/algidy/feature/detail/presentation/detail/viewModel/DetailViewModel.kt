package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.detail.domain.usecase.GetCurrentCategoryUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.GetFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.UpdateFoodDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val foodItemId: String,
    private val markFoodAsConsumedUseCase: MarkFoodAsConsumedUseCase,
    private val markFoodAsWastedUseCase: MarkFoodAsWastedUseCase,
    private val updateFoodDetailUseCase: UpdateFoodDetailUseCase,
    private val getFoodDetailUseCase: GetFoodDetailUseCase,
    private val getCurrentCategoryUseCase: GetCurrentCategoryUseCase
) : BaseViewModel<DetailUiState, DetailEvent, DetailAction>() {
    private val _uiState = MutableStateFlow(DetailUiState())
    override val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    private val stateValue: DetailUiState get() = _uiState.value

    init {
        viewModelScope.launch {
            val foodItem = getFoodDetailUseCase(foodId = foodItemId) ?: FoodItem()
            val category = stateValue.detailFoodItem.categoryId?.let { id ->
                getCurrentCategoryUseCase(id)
            }
            val categoryUiModel = category?.let {
                CategoryUiModel.ByCategory(data = it)
            } ?: CategoryUiModel.Uncategorized

            _uiState.product { copy(detailFoodItem = foodItem, category = categoryUiModel) }
        }
    }

    override fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnDismiss -> updateActionState(DetailOverlay.None)
            DetailAction.OnEditItem -> updateActionState(DetailOverlay.Edit)
            DetailAction.OnConsumeItem -> consumeFoodItem()
            DetailAction.OnWastedItem -> wasteFoodItem()
            is DetailAction.EditEntryAction -> {
                when (action) {
                    is DetailAction.EditEntryAction.OnImageChange -> onImageChange(action.uri)
                }
            }

            DetailAction.OnConsumeFabPress -> updateActionState(DetailOverlay.Consume)
            DetailAction.OnWasteFabPress -> updateActionState(DetailOverlay.Wasted)
        }
    }

    private fun onImageChange(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            updateFoodDetailUseCase(
                newItem = stateValue.detailFoodItem,
                newImageUri = uri.toString()
            )
        }
    }

    private fun updateActionState(state: DetailOverlay) {
        _uiState.product { copy(actionState = state) }
    }

    private fun consumeFoodItem() {
        viewModelScope.launch {
            markFoodAsConsumedUseCase(foodItemId)
            updateActionState(DetailOverlay.None)
        }
    }

    private fun wasteFoodItem() {
        viewModelScope.launch {
            markFoodAsWastedUseCase(foodItemId)
            updateActionState(DetailOverlay.None)
        }
    }
}
