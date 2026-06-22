package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.NotificationFoodItem
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetNotificationEnabled
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CheckExpirationWorker(
    appContext: Context,
    params: WorkerParameters,
    private val getNotificationEnabled: GetNotificationEnabled,
    private val getExpiryFoodUseCase: GetExpiryFoodUseCase,
    private val workerScheduler: WorkerScheduler,
    private val notifier: AlgidyNotifier,
    private val appDispatchers: AppDispatchers,
) : CoroutineWorker(appContext = appContext, params = params) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            try {
                if (getNotificationEnabled()) {
                    Result.success()
                }

                if (runAttemptCount >= 5) {
                    Result.failure()
                }
                val expiringFoods = getExpiryFoodUseCase()
                if (expiringFoods.isEmpty()) {
                    Timber.d("No expiry food!")
                    Result.success()
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


                //Reschedule
                workerScheduler.scheduleCheckExpiryWorker()
                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.retry()
            }
        }
    }
}
