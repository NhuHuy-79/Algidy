package com.nhuhuy.algidy.feature.food_entry.di

import com.nhuhuy.algidy.feature.food_entry.data.FoodEntryDataStore
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.AddCategoryUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.FoodEntryPreferencesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.SaveFoodItemUseCase
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val foodEntryModule = module {
    //data
    factoryOf(::FoodEntryDataStore)

    //usecase
    factoryOf(::ObserveCategoriesUseCase)
    factoryOf(::AddCategoryUseCase)
    factoryOf(::SaveFoodItemUseCase)
    factoryOf(::FoodEntryPreferencesUseCase)

    //viewModel
    viewModelOf(::FoodEntryViewModel)
}
