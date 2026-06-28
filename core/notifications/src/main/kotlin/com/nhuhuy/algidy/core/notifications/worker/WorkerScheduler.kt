package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nhuhuy.algidy.calculateDelayMillis
import com.nhuhuy.algidy.core.datastore.SettingsDataStore
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
}

interface WorkerScheduler {
    fun scheduleCheckExpiryWorker()
    fun scheduleWeeklyReportWorker()
    fun scheduleWeeklyCleanUpFileWorker()

    fun cancelWeeklyReportWorker()
    fun cancelCheckExpiryWorker()
}

class WorkerSchedulerImp(
    private val context: Context,
    private val settingsDataStore: SettingsDataStore,
) : WorkerScheduler {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val workManager by lazy { WorkManager.getInstance(context) }

    override fun cancelWeeklyReportWorker() {
        workManager.cancelUniqueWork(WorkerStrings.WEEKLY_WORKER)
    }

    override fun cancelCheckExpiryWorker() {
        workManager.cancelUniqueWork(WorkerStrings.DAILY_WORKER)
    }

    override fun scheduleCheckExpiryWorker() {
        scope.launch {
            val hour = settingsDataStore.hourFlow.first()
            val minute = settingsDataStore.minuteFlow.first()

            val delay = calculateDelayMillis(
                hour = hour,
                minute = minute
            )

            val request = OneTimeWorkRequestBuilder<CheckExpirationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueueUniqueWork(
                WorkerStrings.DAILY_WORKER,
                ExistingWorkPolicy.REPLACE,
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
            .build()


        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = WorkerStrings.WEEKLY_WORKER,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = weeklyRequest
        )
    }

    override fun scheduleWeeklyCleanUpFileWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val initialDelayMillis = calculateDelayUntilNextSunday9AM()

        val cleanUpRequest = OneTimeWorkRequestBuilder<CleanUpFileWorker>()
            .setConstraints(constraints)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .build()

        // Dùng Unique với OneTimeWork
        workManager.enqueueUniqueWork(
            WorkerStrings.CLEAN_FILE_WORKER,
            ExistingWorkPolicy.KEEP,
            cleanUpRequest
        )
    }

    private fun calculateDelayUntilNextSunday9AM(): Long {
        val now = ZonedDateTime.now(ZoneId.systemDefault())

        var nextTarget = now.with(DayOfWeek.SUNDAY)
            .withHour(9)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        // Nếu thời điểm hiện tại đã vượt qua 9h sáng Chủ Nhật tuần này,
        // thì phải cộng thêm 1 tuần để dời sang Chủ Nhật tuần sau.
        if (now.isAfter(nextTarget) || now.isEqual(nextTarget)) {
            nextTarget = nextTarget.plusWeeks(1)
        }

        return Duration.between(now, nextTarget).toMillis()
    }

}
