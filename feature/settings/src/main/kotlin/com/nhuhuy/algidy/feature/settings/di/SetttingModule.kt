package com.nhuhuy.algidy.feature.settings.di

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.datastore.SettingsDataStoreImpl
import com.nhuhuy.algidy.feature.settings.data.DataBackUpManger
import com.nhuhuy.algidy.feature.settings.data.DatabaseBackUpManager
import com.nhuhuy.algidy.feature.settings.data.DatabaseBackUpManagerImpl
import com.nhuhuy.algidy.feature.settings.data.ImageBackUpManager
import com.nhuhuy.algidy.feature.settings.data.ImageBackUpManagerImpl
import com.nhuhuy.algidy.feature.settings.data.ImageZipHandler
import com.nhuhuy.algidy.feature.settings.data.ImageZipHandlerImpl
import com.nhuhuy.algidy.feature.settings.data.export.CsvDataExporter
import com.nhuhuy.algidy.feature.settings.data.export.JsonDataExporter
import com.nhuhuy.algidy.feature.settings.data.export.ZipDataExporter
import com.nhuhuy.algidy.feature.settings.data.import.ImageZipImporter
import com.nhuhuy.algidy.feature.settings.data.import.ImageZipImporterImpl
import com.nhuhuy.algidy.feature.settings.data.import.JsonDataImporter
import com.nhuhuy.algidy.feature.settings.data.repository.DatabaseDataImporterImpl
import com.nhuhuy.algidy.feature.settings.data.repository.ExportDataProviderImpl
import com.nhuhuy.algidy.feature.settings.domain.DataExporter
import com.nhuhuy.algidy.feature.settings.domain.DataImporter
import com.nhuhuy.algidy.feature.settings.domain.repository.DatabaseDataImporter
import com.nhuhuy.algidy.feature.settings.domain.repository.ExportDataProvider
import com.nhuhuy.algidy.feature.settings.domain.usecase.CheckCapabilityUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ExportDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ImportDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
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

    //New version
    singleOf(::ExportDataProviderImpl) { bind<ExportDataProvider>() }
    singleOf(::CsvDataExporter) { bind<DataExporter>() }
    singleOf(::JsonDataExporter) { bind<DataExporter>() }
    singleOf(::ZipDataExporter) { bind<DataExporter>() }
    singleOf(::ImageZipHandlerImpl) { bind<ImageZipHandler>() }
    singleOf(::ImageZipImporterImpl) { bind<ImageZipImporter>() }
    singleOf(::DatabaseDataImporterImpl) { bind<DatabaseDataImporter>() }
    singleOf(::JsonDataImporter) { bind<DataImporter>() }


    //Use case

    factoryOf(::ObserveSettingStateUseCase)
    factoryOf(::ManageDataUseCase)
    factoryOf(::ImportDataUseCase)
    factoryOf(::DeleteAllDataUseCase)
    factoryOf(::CheckCapabilityUseCase)
    factoryOf(::UpdatePreferencesUseCase)
    factoryOf(::ExportDataUseCase)

    viewModelOf(::SettingsViewModel)
}