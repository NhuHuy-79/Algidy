package com.nhuhuy.algidy.feature.detail.di

import com.nhuhuy.algidy.feature.detail.domain.usecase.GetCategoriesUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.GetFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.ObserveFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.UpdateFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailModule = module {
    //UseCase
    factoryOf(::GetFoodDetailUseCase)
    factoryOf(::MarkFoodAsConsumedUseCase)
    factoryOf(::MarkFoodAsWastedUseCase)
    factoryOf(::UpdateFoodDetailUseCase)
    factoryOf(::ObserveFoodDetailUseCase)
    factoryOf(::GetCategoriesUseCase)

    //ViewModel
    viewModelOf(::DetailViewModel)
}