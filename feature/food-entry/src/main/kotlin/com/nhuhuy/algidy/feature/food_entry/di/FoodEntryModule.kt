package com.nhuhuy.algidy.feature.food_entry.di

import com.nhuhuy.algidy.core.domain.usecase.food.GetFoodByIdUseCase
import com.nhuhuy.algidy.feature.food_entry.data.FoodEntryDataStore
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.AddCategoryUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.FoodEntryPreferencesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.GetCategoriesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.SaveFoodItemUseCase
import com.nhuhuy.algidy.feature.food_entry.presentation.model.EntryUiModel
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val foodEntryModule = module {
    //data
    single { FoodEntryDataStore(androidContext()) }

    //usecase
    factoryOf(::ObserveCategoriesUseCase)
    factoryOf(::AddCategoryUseCase)
    factoryOf(::GetCategoriesUseCase)
    factoryOf(::SaveFoodItemUseCase)
    factoryOf(::FoodEntryPreferencesUseCase)
    factoryOf(::GetFoodByIdUseCase)

    //viewModel
    viewModel { (foodId: EntryUiModel) ->
        FoodEntryViewModel(
            entryUiModel = get(),
            addCategoryUseCase = get(),
            saveFoodItemUseCase = get(),
            foodEntryPreferencesUseCase = get(),
            observeCategoriesUseCase = get(),
        )
    }
}
