package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.LocalMediaStorageImpl
import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.data.repository.CategoryRepositoryImpl
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.repository.FoodRepositoryImpl
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.DefaultAppDispatchers
import com.nhuhuy.algidy.core.datastore.dataStore
import com.nhuhuy.algidy.core.datastore.migration.AppearanceMigrator
import com.nhuhuy.algidy.core.datastore.migration.BaseMigrator
import com.nhuhuy.algidy.core.datastore.migration.GeneralMigrator
import com.nhuhuy.algidy.core.datastore.migration.NotificationMigrator
import com.nhuhuy.algidy.core.datastore.model.AppearanceDataStore
import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.datastore.model.GeneralDataStore
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences
import com.nhuhuy.algidy.core.datastore.utils.BaseDataStore
import com.nhuhuy.algidy.feature.scanner.data.MLKitBarcodeScanner
import com.nhuhuy.algidy.feature.scanner.data.MLKitFoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val dataModule = module {
    singleOf(::AppNewFeaturesReader)
    singleOf(::FoodRepositoryImpl) { bind<FoodRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }
    singleOf(::DefaultAppDispatchers) { bind<AppDispatchers>() }
    single<BarcodeScanner> { MLKitBarcodeScanner(get()) }
    single<FoodDateScanner> { MLKitFoodDateScanner(get(), get()) }
    singleOf(::LocalMediaStorageImpl) { bind<LocalMediaStorage>() }

    //Datastore
    single { androidApplication().dataStore }
    single {
        Json {
            decodeEnumsCaseInsensitive = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }

    singleOf(::NotificationDataStore) { bind<BaseDataStore<NotificationPreferences>>() }
    singleOf(::AppearanceDataStore) { bind<BaseDataStore<AppearancePreferences>>() }
    singleOf(::GeneralDataStore) { bind<BaseDataStore<GeneralPreferences>>() }

    //Datastore - Migration
    singleOf(::NotificationMigrator) { bind<BaseMigrator>() }
    singleOf(::AppearanceMigrator) { bind<BaseMigrator>() }
    singleOf(::GeneralMigrator) { bind<BaseMigrator>() }
}
