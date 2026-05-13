package com.nhuhuy.algidy.feature.analytics.di

import com.nhuhuy.algidy.feature.analytics.data.repository.AnalyticsRepositoryImpl
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::AnalyticsRepositoryImpl) { bind<AnalyticsRepository>() }
    //ViewModel
    viewModelOf(::AnalyticsViewModel)

}