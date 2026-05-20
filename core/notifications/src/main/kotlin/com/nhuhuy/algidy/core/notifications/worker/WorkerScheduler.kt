package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
}

class WorkerSchedulerImp(
    private val context: Context
) : WorkerScheduler {
    private val workManager by lazy { WorkManager.getInstance(context) }

    override fun scheduleCheckExpiryWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val dailyRequest = PeriodicWorkRequestBuilder<CheckExpirationWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = WorkerStrings.DAILY_WORKER,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = dailyRequest
        )
    }

    override fun scheduleWeeklyReportWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        // 1. Tính toán độ trễ cho đến 9:00 sáng Chủ Nhật tiếp theo
        val initialDelayMillis = calculateDelayUntilNextSunday9AM()

        // 2. Tạo Request với Initial Delay
        val weeklyRequest = PeriodicWorkRequestBuilder<WeeklyReportWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(
                initialDelayMillis,
                TimeUnit.MILLISECONDS
            ) // Ép Worker đợi đến Chủ Nhật
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
