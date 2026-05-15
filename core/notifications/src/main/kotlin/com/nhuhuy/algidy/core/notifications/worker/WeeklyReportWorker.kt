package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetWeeklySummaryUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class WeeklyReportWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params), KoinComponent {

    private val getWeeklySummaryUseCase: GetWeeklySummaryUseCase by inject()
    private val notifier: AlgidyNotifier by inject()

    override suspend fun doWork(): Result {
        return try {
            if (runAttemptCount >= 5) {
                return Result.failure()
            }
            val (consumed, wasted) = getWeeklySummaryUseCase()

            if (consumed == 0 && wasted == 0) {
                return Result.success()
            }

            notifier.showWeeklySummary(consumed, wasted)
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error showing weekly summary")
            Result.retry()
        }
    }
}
