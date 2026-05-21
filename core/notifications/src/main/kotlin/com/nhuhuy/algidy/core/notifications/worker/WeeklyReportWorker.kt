package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetNotificationEnabled
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetWeeklySummaryUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeeklyReportWorker(
    appContext: Context,
    params: WorkerParameters,
    private val notifier: AlgidyNotifier,
    private val getNotificationEnabled: GetNotificationEnabled,
    private val getWeeklySummaryUseCase: GetWeeklySummaryUseCase,
    private val appDispatchers: AppDispatchers
) : CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                if (getNotificationEnabled()) {
                    Result.success()
                }
                if (runAttemptCount >= 5) {
                    Result.failure()
                }
                val (consumed, wasted) = getWeeklySummaryUseCase()

                if (consumed == 0 && wasted == 0) {
                    Result.success()
                }

                notifier.showWeeklySummary(consumed, wasted)
                Result.success()
            } catch (e: Exception) {
                Timber.e(e, "Error showing weekly summary")
                Result.retry()
            }
        }
    }
}
