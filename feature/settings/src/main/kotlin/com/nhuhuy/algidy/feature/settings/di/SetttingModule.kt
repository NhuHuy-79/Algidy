package com.nhuhuy.algidy.feature.settings.di

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingModule = module {
    single { SettingsDataStore(context = androidContext()) }
    viewModelOf(::SettingsViewModel)
}