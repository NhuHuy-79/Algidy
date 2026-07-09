package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.notifications.domain.usecase.DeleteOldFoodUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber


class DeleteOldFoodWorker(
    private val appDispatchers: AppDispatchers,
    private val deleteOldFoodUseCase: DeleteOldFoodUseCase,
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                deleteOldFoodUseCase()
                    .onSuccess {
                        return@withContext Result.success()
                    }
                    .onFailure {
                        return@withContext Result.failure()
                    }
                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure()
            }
        }
    }
}