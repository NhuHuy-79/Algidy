package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.repository.FoodRepositoryImpl
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.DefaultAppDispatchers
import org.koin.dsl.module

val dataModule = module {
    single<FoodRepository> { FoodRepositoryImpl(get(), get(), get()) }
    single<AppDispatchers> { DefaultAppDispatchers() }
}
