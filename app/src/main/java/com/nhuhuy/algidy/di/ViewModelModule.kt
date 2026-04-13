package com.nhuhuy.algidy.di

import com.nhuhuy.aldidy.feature.inventory.presentation.viewmodel.InventoryViewModel
import com.nhuhuy.algidy.feature.detail.presentation.viewModel.DetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::InventoryViewModel)
    viewModelOf(::DetailViewModel)
}