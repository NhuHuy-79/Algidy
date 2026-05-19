package com.nhuhuy.algidy.feature.settings.di

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.datastore.SettingsDataStoreImpl
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SelectSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingModule = module {
    single<SettingsDataStore> { SettingsDataStoreImpl(context = androidContext()) }

    factoryOf(::ObserveSettingStateUseCase)
    factoryOf(::SelectSettingUseCase)
    factoryOf(::SetToggleSettingUseCase)
    viewModelOf(::SettingsViewModel)
}