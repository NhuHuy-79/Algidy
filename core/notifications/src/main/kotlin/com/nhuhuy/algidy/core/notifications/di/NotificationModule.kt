package com.nhuhuy.algidy.core.notifications.di

import com.nhuhuy.algidy.core.notifications.NotificationHelper
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import com.nhuhuy.algidy.core.notifications.worker.CheckExpirationWorker
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler
import com.nhuhuy.algidy.core.notifications.worker.WorkerSchedulerImp
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val notificationModule = module {
    singleOf(::WorkerSchedulerImp) bind WorkerScheduler::class
    factoryOf(::NotificationHelper)
    //UseCase
    factoryOf(::GetExpiryFoodUseCase)
    //Worker
    workerOf(::CheckExpirationWorker)
}