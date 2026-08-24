package com.nhuhuy.algidy.feature.inventory.di

import com.nhuhuy.algidy.core.domain.usecase.category.AddCategoryUseCase
import com.nhuhuy.algidy.core.domain.usecase.category.DeleteCategoryUseCase
import com.nhuhuy.algidy.core.domain.usecase.category.EditCategoryUseCase
import com.nhuhuy.algidy.core.domain.usecase.category.ObserveCategoriesUseCase
import com.nhuhuy.algidy.core.domain.usecase.food.CreateFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.data.repository.SearchRepositoryImp
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetHistoryResultUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetInventoryPreferenceUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.DeleteFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.ObserveFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.SearchFoodUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchViewModel
import com.nhuhuy.algidy.feature.inventory.presentation.shared.DetailBottomSheetViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val inventoryModule = module {
    //repository
    singleOf(::SearchRepositoryImp) { bind<SearchRepository>() }
    //usecase
    factoryOf(::GetInventoryPreferenceUseCase)
    factoryOf(::DeleteCategoryUseCase)
    factoryOf(::EditCategoryUseCase)
    factoryOf(::GetHistoryResultUseCase)
    factoryOf(::SearchFoodUseCase)
    factoryOf(::CreateFoodItemUseCase)
    factoryOf(::AddCategoryUseCase)
    factoryOf(::DeleteFoodItemUseCase)
    factoryOf(::ObserveFoodItemUseCase)
    factoryOf(::ObserveCategoriesUseCase)
    factoryOf(::ObserveSettingDataUseCase)
    factoryOf(::MarkFoodAsConsumedUseCase)
    factoryOf(::MarkFoodAsWastedUseCase)
    //viewModel
    viewModelOf(::InventoryViewModel)
    viewModelOf(::SearchViewModel)
    viewModel { (foodItem: FoodUiModel) ->
        DetailBottomSheetViewModel(
            foodItem = foodItem,
            markFoodAsConsumedUseCase = get(),
            markFoodAsWastedUseCase = get()
        )
    }
}
