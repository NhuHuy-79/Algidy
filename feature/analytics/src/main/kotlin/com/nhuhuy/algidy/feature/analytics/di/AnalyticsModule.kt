package com.nhuhuy.algidy.feature.analytics.di

import com.nhuhuy.algidy.feature.analytics.data.repository.FoodAnalyticsRepositoryImpl
import com.nhuhuy.algidy.feature.analytics.domain.repository.FoodAnalyticsRepository
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Analytics feature.
 * Defines dependencies for repository, individual UseCases, and the ViewModel.
 */
val analyticsModule = module {
    // Repository
    singleOf(::FoodAnalyticsRepositoryImpl) { bind<FoodAnalyticsRepository>() }

    // ViewModel
    viewModelOf(::AnalyticsViewModel)
}
