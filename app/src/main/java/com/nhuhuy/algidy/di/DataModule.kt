package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.LocalMediaStorageImpl
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.repository.FoodRepositoryImpl
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.DefaultAppDispatchers
import com.nhuhuy.algidy.feature.scanner.data.MLKitBarcodeScanner
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner
import org.koin.dsl.module

val dataModule = module {
    single<FoodRepository> { FoodRepositoryImpl(get(), get(), get()) }
    single<AppDispatchers> { DefaultAppDispatchers() }
    single<BarcodeScanner> { MLKitBarcodeScanner(get()) }
    single<LocalMediaStorage> { LocalMediaStorageImpl(get(), get()) }
}
