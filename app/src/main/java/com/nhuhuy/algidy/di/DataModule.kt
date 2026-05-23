package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.LocalMediaStorageImpl
import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.data.repository.CategoryRepositoryImpl
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.repository.FoodRepositoryImpl
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.DefaultAppDispatchers
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegate
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegateImpl
import com.nhuhuy.algidy.feature.scanner.data.MLKitBarcodeScanner
import com.nhuhuy.algidy.feature.scanner.data.MLKitFoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    //delegate
    factoryOf(::FoodEntryDelegateImpl) bind FoodEntryDelegate::class
    single<FoodRepository> { FoodRepositoryImpl(get(), get(), get()) }
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    single<AppDispatchers> { DefaultAppDispatchers() }
    single<BarcodeScanner> { MLKitBarcodeScanner(get()) }
    single<FoodDateScanner> { MLKitFoodDateScanner(get(), get()) }
    singleOf(::LocalMediaStorageImpl) bind LocalMediaStorage::class
}
