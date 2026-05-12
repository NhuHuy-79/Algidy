package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsViewModel
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailViewModel
import com.nhuhuy.algidy.feature.inventory.presentation.viewmodel.InventoryViewModel
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel.ConfirmViewModel
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::InventoryViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::ScannerViewModel)
    viewModelOf(::ConfirmViewModel)
    viewModelOf(::AnalyticsViewModel)
}
