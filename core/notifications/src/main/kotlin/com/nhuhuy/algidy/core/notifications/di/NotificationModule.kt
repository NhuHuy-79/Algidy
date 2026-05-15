package com.nhuhuy.algidy.core.notifications.di

import com.nhuhuy.algidy.core.notifications.data.AlgidyNotificationFactory
import com.nhuhuy.algidy.core.notifications.data.AlgidyNotifierImp
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetWeeklySummaryUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.UpdateFoodStatusUseCase
import com.nhuhuy.algidy.core.notifications.worker.CheckExpirationWorker
import com.nhuhuy.algidy.core.notifications.worker.WeeklyReportWorker
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler
import com.nhuhuy.algidy.core.notifications.worker.WorkerSchedulerImp
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val notificationModule = module {
    singleOf(::WorkerSchedulerImp) bind WorkerScheduler::class
    singleOf(::AlgidyNotificationFactory)
    singleOf(::AlgidyNotifierImp) bind AlgidyNotifier::class

    // UseCase
    factoryOf(::GetExpiryFoodUseCase)
    factoryOf(::UpdateFoodStatusUseCase)
    factoryOf(::GetWeeklySummaryUseCase)

    // Workers
    workerOf(::CheckExpirationWorker)
    workerOf(::WeeklyReportWorker)
}
