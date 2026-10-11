package com.nhuhuy.algidy.widget.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.widget.usecase.GetFoodsUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber

class WasteWeeklyAllFoodsWorker(
    private val appDispatchers: AppDispatchers,
    private val foodRepository: FoodRepository,
    private val getFoodsUseCase: GetFoodsUseCase,
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                val list = getFoodsUseCase.getThisWeek().map { it.id }
                foodRepository.updateFoodStatusList(list, FoodStatus.WASTED)
                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure()
            }
        }
    }
}