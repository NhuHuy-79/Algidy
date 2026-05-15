package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.NotificationFoodItem
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CheckExpirationWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext = appContext, params = params), KoinComponent {
    private val getExpiryFoodUseCase: GetExpiryFoodUseCase by inject()
    private val notifier: AlgidyNotifier by inject()

    override suspend fun doWork(): Result {
        return try {
            if (runAttemptCount >= 5) {
                return Result.failure()
            }
            val expiringFoods = getExpiryFoodUseCase(dayWarnings = 3)
            if (expiringFoods.isEmpty()) {
                Timber.d("No expiry food!")
                return Result.success()
            }

            val currentTime = System.currentTimeMillis()

            val expiredToday = expiringFoods.filter { food ->
                val diff = food.expiryDate - currentTime
                TimeUnit.MILLISECONDS.toDays(diff) < 1
            }

            val expiringSoon = expiringFoods.filter { food ->
                val diff = food.expiryDate - currentTime
                TimeUnit.MILLISECONDS.toDays(diff) >= 1
            }

            expiredToday.forEach { food ->
                notifier.showActionableExpiryPrompt(
                    foodId = food.id,
                    foodName = food.name,
                    image = null
                )
            }

            if (expiringSoon.isNotEmpty()) {
                val notificationItems = expiringSoon.map { food ->
                    val diff = food.expiryDate - currentTime
                    val daysLeft = TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
                    NotificationFoodItem(
                        id = food.id,
                        name = food.name,
                        daysLeft = daysLeft
                    )
                }
                notifier.showExpiringItemsAlert(notificationItems)
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.retry()
        }
    }
}
