package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.domain.usecase.CheckUpdateUseCase
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import kotlinx.coroutines.withContext

class CheckUpdateVersionWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val appNewFeaturesReader: AppNewFeaturesReader,
    private val appDispatchers: AppDispatchers,
    private val notifier: AlgidyNotifier,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                val currentVersion = appNewFeaturesReader.currentVersionName
                checkUpdateUseCase()
                    .onSuccess { newVersion ->
                        if (newVersion != null && newVersion != currentVersion) {
                            notifier.showNewUpdateVersion()
                        }
                    }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}