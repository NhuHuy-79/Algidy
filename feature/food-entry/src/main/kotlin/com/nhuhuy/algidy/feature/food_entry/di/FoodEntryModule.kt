package com.nhuhuy.algidy.feature.food_entry.di

import com.nhuhuy.algidy.feature.food_entry.domain.usecase.AddCategoryUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.ObserveCategoriesUseCase
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.SaveFoodItemUseCase
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val foodEntryModule = module {
    factoryOf(::ObserveCategoriesUseCase)
    factoryOf(::AddCategoryUseCase)
    factoryOf(::SaveFoodItemUseCase)

    viewModel { params ->
        FoodEntryViewModel(
            initialFoodItem = params.getOrNull(),
            observeCategoriesUseCase = get(),
            saveFoodItemUseCase = get(),
            addCategoryUseCase = get()
        )
    }
}
