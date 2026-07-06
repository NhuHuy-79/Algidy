package com.nhuhuy.algidy.feature.settings.di

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.datastore.SettingsDataStoreImpl
import com.nhuhuy.algidy.feature.settings.data.DataBackUpManger
import com.nhuhuy.algidy.feature.settings.data.DatabaseBackUpManager
import com.nhuhuy.algidy.feature.settings.data.DatabaseBackUpManagerImpl
import com.nhuhuy.algidy.feature.settings.data.ImageBackUpManager
import com.nhuhuy.algidy.feature.settings.data.ImageBackUpManagerImpl
import com.nhuhuy.algidy.feature.settings.domain.usecase.CheckCapabilityUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ImportDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SelectSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.UpdatePreferencesUseCase
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingModule = module {
    single<SettingsDataStore> { SettingsDataStoreImpl(context = androidContext()) }
    singleOf(::DatabaseBackUpManagerImpl) { bind<DatabaseBackUpManager>() }
    singleOf(::ImageBackUpManagerImpl) { bind<ImageBackUpManager>() }
    singleOf(::DataBackUpManger)

    factoryOf(::ObserveSettingStateUseCase)
    factoryOf(::SelectSettingUseCase)
    factoryOf(::SetToggleSettingUseCase)
    factoryOf(::ManageDataUseCase)
    factoryOf(::ImportDataUseCase)
    factoryOf(::DeleteAllDataUseCase)
    factoryOf(::CheckCapabilityUseCase)
    factoryOf(::UpdatePreferencesUseCase)

    viewModelOf(::SettingsViewModel)
}