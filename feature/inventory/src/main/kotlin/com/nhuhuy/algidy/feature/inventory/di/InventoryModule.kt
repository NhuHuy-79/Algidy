package com.nhuhuy.algidy.feature.inventory.di

import com.nhuhuy.algidy.feature.inventory.data.repository.SearchRepositoryImp
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetInventoryPreferenceUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.UpdateSearchHistoryUseCase
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
    factoryOf(::UpdateSearchHistoryUseCase)
    factoryOf(::ObserveSettingDataUseCase)
    //viewModel
    viewModelOf(::InventoryViewModel)
    viewModelOf(::SearchViewModel)
    viewModel { (foodItem: FoodUiModel) ->
        DetailBottomSheetViewModel(
            foodItem = foodItem,
            foodRepository = get()
        )
    }
}
