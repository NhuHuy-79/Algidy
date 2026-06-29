package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetNotificationPreferenceUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetWeeklySummaryUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeeklyReportWorker(
    appContext: Context,
    params: WorkerParameters,
    private val notifier: AlgidyNotifier,
    private val getNotificationPreferenceUseCase: GetNotificationPreferenceUseCase,
    private val getWeeklySummaryUseCase: GetWeeklySummaryUseCase,
    private val appDispatchers: AppDispatchers
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                if (getNotificationPreferenceUseCase.getWeeklyReportEnabled() && getNotificationPreferenceUseCase.getNotificationEnabled()) {
                    return@withContext Result.success()
                }
                if (runAttemptCount >= 5) {
                    return@withContext Result.failure()
                }
                val (consumed, wasted) = getWeeklySummaryUseCase()

                if (consumed == 0 && wasted == 0) {
                    return@withContext Result.success()
                }

                notifier.showWeeklySummary(consumed, wasted)
                Result.success()
            } catch (e: Exception) {
                Timber.e(e, "Error showing weekly summary")
                Result.retry()
            } finally {
                //
            }
        }
    }
}
