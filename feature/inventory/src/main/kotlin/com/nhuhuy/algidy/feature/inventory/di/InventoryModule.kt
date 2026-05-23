package com.nhuhuy.algidy.feature.inventory.di

import com.nhuhuy.algidy.feature.inventory.data.repository.SearchRepositoryImp
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import com.nhuhuy.algidy.feature.inventory.domain.usecase.CreateFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.DeleteFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetHistoryResultUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.ObserveSettingDataUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.SearchFoodUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val inventoryModule = module {
    //repository
    singleOf(::SearchRepositoryImp) bind SearchRepository::class
    //usecase
    factoryOf(::GetHistoryResultUseCase)
    factoryOf(::SearchFoodUseCase)
    factoryOf(::CreateFoodItemUseCase)
    factoryOf(::DeleteFoodItemUseCase)
    factoryOf(::ObserveFoodItemUseCase)
    factoryOf(::ObserveSettingDataUseCase)
    //viewModel
    viewModelOf(::InventoryViewModel)
    viewModelOf(::SearchViewModel)
}