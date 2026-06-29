package com.nhuhuy.algidy.core.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.NotificationFoodItem
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetExpiryFoodUseCase
import com.nhuhuy.algidy.core.notifications.domain.usecase.GetNotificationPreferenceUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CheckExpirationWorker(
    appContext: Context,
    params: WorkerParameters,
    private val getNotificationPreferenceUseCase: GetNotificationPreferenceUseCase,
    private val getExpiryFoodUseCase: GetExpiryFoodUseCase,
    private val workerScheduler: WorkerScheduler,
    private val notifier: AlgidyNotifier,
    private val appDispatchers: AppDispatchers,
) : CoroutineWorker(appContext = appContext, params = params) {
    override suspend fun doWork(): Result {
        return withContext(appDispatchers.io) {
            Timber.d("Expiration Worker Check!")
            try {
                if (!getNotificationPreferenceUseCase()) {
                    return@withContext Result.success()
                }

                if (runAttemptCount >= 5) {
                    return@withContext Result.failure()
                }
                val expiringFoods = getExpiryFoodUseCase()
                if (expiringFoods.isEmpty()) {
                    Timber.d("No expiry food!")
                    return@withContext Result.success()
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
                        uriPath = food.imageUri
                    )
                }

                if (expiringSoon.isNotEmpty()) {
                    val notificationItems = expiringSoon.map { food ->
                        NotificationFoodItem(
                            id = food.id,
                            name = food.name,
                            daysLeft = food.getRemainingDays(),
                            imageUri = food.imageUri
                        )
                    }
                    notifier.showExpiringItemsAlert(notificationItems)
                }
                //Reschedule
                Result.success()
            } catch (e: Exception) {
                Timber.e(e)
                Result.retry()
            } finally {
                workerScheduler.scheduleCheckExpiryWorker()
            }
        }
    }
}
