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
import com.nhuhuy.algidy.feature.scanner.data.MLKitBarcodeScanner
import com.nhuhuy.algidy.feature.scanner.data.MLKitFoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AppNewFeaturesReader)
    singleOf(::FoodRepositoryImpl) { bind<FoodRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }
    singleOf(::DefaultAppDispatchers) { bind<AppDispatchers>() }
    single<BarcodeScanner> { MLKitBarcodeScanner(get()) }
    single<FoodDateScanner> { MLKitFoodDateScanner(get(), get()) }
    singleOf(::LocalMediaStorageImpl) { bind<LocalMediaStorage>() }
}
