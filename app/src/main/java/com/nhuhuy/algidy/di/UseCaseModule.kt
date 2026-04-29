package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.feature.detail.domain.usecase.GetFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.UpdateFoodDetailUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    //Detail Module
    factoryOf(::GetFoodDetailUseCase)
    factoryOf(::MarkFoodAsConsumedUseCase)
    factoryOf(::MarkFoodAsWastedUseCase)
    factoryOf(::UpdateFoodDetailUseCase)
}