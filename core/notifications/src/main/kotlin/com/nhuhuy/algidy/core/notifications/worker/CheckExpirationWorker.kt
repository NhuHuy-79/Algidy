package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.notifications.NotificationHelper
import com.nhuhuy.algidy.core.notifications.R
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class CheckExpirationWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext = appContext, params = params), KoinComponent {
    private val getExpiryFoodUseCase: GetExpiryFoodUseCase by inject()
    private val notificationHelper: NotificationHelper by inject()
    override suspend fun doWork(): Result {
        return try {
            val expiringFoods = getExpiryFoodUseCase()
            if (expiringFoods.isEmpty()) {
                Timber.d("No expiry food!")
                return Result.success()
            }
            val message =
                formatNotificationMessage(context = applicationContext, foods = expiringFoods)
            notificationHelper.showExpiryNotification(
                title = applicationContext.getString(R.string.notif_expiry_title),
                message = message
            )
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.retry()
        }
    }

    private fun formatNotificationMessage(context: Context, foods: List<FoodItem>): String {
        val sampleNames = foods.take(2).joinToString(", ") { it.name }

        return if (foods.size > 2) {
            val remainingCount = foods.size - 2
            context.getString(R.string.notif_expiry_many_items, sampleNames, remainingCount)
        } else {
            context.getString(R.string.notif_expiry_few_items, sampleNames)
        }
    }

}