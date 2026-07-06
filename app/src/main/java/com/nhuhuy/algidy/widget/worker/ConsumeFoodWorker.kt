package com.nhuhuy.algidy.widget.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.MarkFoodAsConsumedUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber

class ConsumeFoodWorker(
    private val appDispatchers: AppDispatchers,
    private val consumedUseCase: MarkFoodAsConsumedUseCase,
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                val foodId = inputData.getString(CallbackScheduler.FOOD_ID)
                    ?: return@withContext Result.failure()
                consumedUseCase(foodId = foodId)
                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure()
            }
        }
    }
}