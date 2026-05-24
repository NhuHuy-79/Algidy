package com.nhuhuy.algidy.feature.scanner.di

import com.nhuhuy.algidy.feature.scanner.domain.usecase.CreateFoodItemFromDateUseCase
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanBarcodeUseCase
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanFoodDateUseCase
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val scannerModule = module {
    //usecase
    factoryOf(::ScanBarcodeUseCase)
    factoryOf(::ScanFoodDateUseCase)
    factoryOf(::CreateFoodItemFromDateUseCase)

    //viewmodel
    viewModelOf(::ScannerViewModel)
}