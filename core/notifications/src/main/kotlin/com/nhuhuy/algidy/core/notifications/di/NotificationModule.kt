package com.nhuhuy.algidy.core.notifications.di

import coil3.ImageLoader
import com.nhuhuy.algidy.core.notifications.data.AlgidyNotificationFactory
import com.nhuhuy.algidy.core.notifications.data.AlgidyNotifierImp
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.usecase.DeleteOldFoodUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetNotificationPreferenceUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetWeeklySummaryUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.UpdateFoodStatusUseCase
import com.nhuhuy.algidy.core.notifications.worker.CheckExpirationWorker
import com.nhuhuy.algidy.core.notifications.worker.CleanUpFileWorker
import com.nhuhuy.algidy.core.notifications.worker.DeleteOldFoodWorker
import com.nhuhuy.algidy.core.notifications.worker.WeeklyReportWorker
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler
import com.nhuhuy.algidy.core.notifications.worker.WorkerSchedulerImp
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val notificationModule = module {
    singleOf(::WorkerSchedulerImp) { bind<WorkerScheduler>() }
    singleOf(::AlgidyNotifierImp) { bind<AlgidyNotifier>() }
    singleOf(::AlgidyNotificationFactory)
    single<ImageLoader> {
        ImageLoader(context = get())
    }


    // UseCase
    factoryOf(::GetNotificationPreferenceUseCase)
    factoryOf(::GetExpiryFoodUseCase)
    factoryOf(::UpdateFoodStatusUseCase)
    factoryOf(::GetWeeklySummaryUseCase)
    factoryOf(::DeleteOldFoodUseCase)

    // Workers
    workerOf(::CheckExpirationWorker)
    workerOf(::WeeklyReportWorker)
    workerOf(::CleanUpFileWorker)
    workerOf(::DeleteOldFoodWorker)
}
