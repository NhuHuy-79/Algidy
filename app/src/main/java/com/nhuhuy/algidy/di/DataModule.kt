package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.repository.FoodRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<FoodRepository> { FoodRepositoryImpl(get()) }

}