package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.domain.usecase.CheckUpdateUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::CheckUpdateUseCase)
}