package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.AppViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::AppViewModel)
}