package com.nhuhuy.algidy.di

import com.nhuhuy.aldidy.feature.inventory.presentation.viewmodel.InventoryViewModel
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailViewModel
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::InventoryViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::ScannerViewModel)
}