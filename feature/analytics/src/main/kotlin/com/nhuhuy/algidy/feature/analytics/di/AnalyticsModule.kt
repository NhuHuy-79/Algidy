package com.nhuhuy.algidy.feature.analytics.di

import com.nhuhuy.algidy.feature.analytics.data.repository.AnalyticsRepositoryImpl
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetDailyFreshnessStatsUseCase
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetSummaryStatsUseCase
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetWastedByCategoryUseCase
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetWeeklySpoilageHistoryUseCase
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
    singleOf(::AnalyticsRepositoryImpl) { bind<AnalyticsRepository>() }
    
    // Individual UseCases
    singleOf(::GetSummaryStatsUseCase)
    singleOf(::GetWastedByCategoryUseCase)
    singleOf(::GetDailyFreshnessStatsUseCase)
    singleOf(::GetWeeklySpoilageHistoryUseCase)
    
    // ViewModel
    viewModelOf(::AnalyticsViewModel)
}
