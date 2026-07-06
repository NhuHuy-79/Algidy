package com.nhuhuy.algidy.widget.worker

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class CallbackScheduler(
    private val context: Context
) {
    private val workerManager by lazy {
        WorkManager.getInstance(context)
    }

    companion object {
        const val FOOD_ID = "food_id"
        const val REFRESH = "refresh"
    }


    fun scheduleConsumeFoodWorker(foodId: String) {
        val request = OneTimeWorkRequestBuilder<ConsumeFoodWorker>()
            .setInputData(
                workDataOf(
                    FOOD_ID to foodId,
                    REFRESH to true
                )
            )
            .build()

        workerManager.enqueueUniqueWork(
            uniqueWorkName = "consume_food_worker",
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request
        )
    }

    fun scheduleWasteWeeklyAllFoodsWorker() {
        val request = OneTimeWorkRequestBuilder<WasteWeeklyAllFoodsWorker>()
            .build()
        workerManager.enqueueUniqueWork(
            uniqueWorkName = "waste_weekly_all_foods_worker",
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = request
        )
    }

}