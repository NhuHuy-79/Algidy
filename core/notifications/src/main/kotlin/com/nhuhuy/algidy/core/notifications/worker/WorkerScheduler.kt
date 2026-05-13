package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkerStrings {
    const val dailyWorker = "DAILY_CHECK_EXPIRY_WORKER"
}

interface WorkerScheduler {
    fun scheduleCheckExpiryWorker()
}

class WorkerSchedulerImp(
    private val context: Context
) : WorkerScheduler {
    private val workManager by lazy { WorkManager.getInstance(context) }
    override fun scheduleCheckExpiryWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(requiresBatteryNotLow = true)
            .build()

        val dailyRequest = PeriodicWorkRequestBuilder<CheckExpirationWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = WorkerStrings.dailyWorker,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = dailyRequest
        )
    }

}