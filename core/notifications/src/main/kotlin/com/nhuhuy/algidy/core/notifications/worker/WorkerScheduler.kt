package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nhuhuy.algidy.calculateDelayMillis
import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object WorkerStrings {
    const val DAILY_WORKER = "DAILY_CHECK_EXPIRY_WORKER"
    const val WEEKLY_WORKER = "WEEKLY_SUMMARY_WORKER"
    const val CLEAN_FILE_WORKER = "CLEAN_FILE_WORKER"
    const val DELETE_OLD_FOOD_WORKER = "DELETE_OLD_FOOD_WORKER"
}

interface WorkerScheduler {
    fun scheduleCheckExpiryWorker(forceReplace: Boolean = false)
    fun scheduleWeeklyReportWorker()
    fun scheduleWeeklyCleanUpFileWorker()
    fun scheduleWeeklyDeleteFoodWorker()
}

class WorkerSchedulerImp(
    private val context: Context,
    private val notificationDataStore: NotificationDataStore,
) : WorkerScheduler {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val workManager by lazy { WorkManager.getInstance(context) }

    override fun scheduleCheckExpiryWorker(forceReplace: Boolean) {
        scope.launch {
            val prefs = notificationDataStore.preferencesFlow.first()
            if (!prefs.enableNotification) {
                workManager.cancelUniqueWork(WorkerStrings.DAILY_WORKER)
                return@launch
            }

            val hour = prefs.hour
            val minute = prefs.minutes

            val delay = calculateDelayMillis(
                hour = hour,
                minute = minute
            )

            val request = OneTimeWorkRequestBuilder<CheckExpirationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(WorkerStrings.DAILY_WORKER)
                .build()

            workManager.enqueueUniqueWork(
                WorkerStrings.DAILY_WORKER,
                if (forceReplace) ExistingWorkPolicy.REPLACE else ExistingWorkPolicy.KEEP,
                request
            )
        }
    }

    override fun scheduleWeeklyReportWorker() {
        val initialDelayMillis = calculateDelayUntilNextSunday9AM()

        val weeklyRequest = PeriodicWorkRequestBuilder<WeeklyReportWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(
                initialDelayMillis,
                TimeUnit.MILLISECONDS
            )
            .addTag(WorkerStrings.WEEKLY_WORKER)
            .build()


        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = WorkerStrings.WEEKLY_WORKER,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = weeklyRequest
        )
    }

    override fun scheduleWeeklyCleanUpFileWorker() {
        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(true)
            .setRequiresBatteryNotLow(true)
            .build()
        val initialDelayMillis = calculateDelayUntilNextSunday9AM()

        val cleanUpRequest = PeriodicWorkRequestBuilder<CleanUpFileWorker>(
            7, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .addTag(WorkerStrings.CLEAN_FILE_WORKER)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkerStrings.CLEAN_FILE_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            cleanUpRequest
        )
    }

    override fun scheduleWeeklyDeleteFoodWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()

        val initialDelayMillis = calculateDelayUntilNextSunday9AM()

        val deleteRequest = PeriodicWorkRequestBuilder<DeleteOldFoodWorker>(
            7, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .addTag(WorkerStrings.DELETE_OLD_FOOD_WORKER)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkerStrings.DELETE_OLD_FOOD_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            deleteRequest
        )
    }

    private fun calculateDelayUntilNextSunday9AM(): Long {
        val now = ZonedDateTime.now(ZoneId.systemDefault())

        var nextTarget = now.with(DayOfWeek.SUNDAY)
            .withHour(9)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        if (now.isAfter(nextTarget) || now.isEqual(nextTarget)) {
            nextTarget = nextTarget.plusWeeks(1)
        }

        return Duration.between(now, nextTarget).toMillis()
    }

}
