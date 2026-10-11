package com.nhuhuy.algidy.widget.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus
import kotlinx.coroutines.withContext
import timber.log.Timber

class ConsumeFoodWorker(
    private val appDispatchers: AppDispatchers,
    private val foodRepository: FoodRepository,
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                val foodId = inputData.getString(CallbackScheduler.FOOD_ID)
                    ?: return@withContext Result.failure()
                foodRepository.updateFoodStatus(id = foodId, newStatus = FoodStatus.CONSUMED)
                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure()
            }
        }
    }
}