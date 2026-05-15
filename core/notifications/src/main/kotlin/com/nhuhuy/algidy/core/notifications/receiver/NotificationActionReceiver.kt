package com.nhuhuy.algidy.core.notifications.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.notifications.data.AlgidyNotifierImp
import com.nhuhuy.algidy.core.notifications.domain.usecase.UpdateFoodStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationActionReceiver : BroadcastReceiver(), KoinComponent {

    private val updateFoodStatusUseCase: UpdateFoodStatusUseCase by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val foodId = intent.getStringExtra(AlgidyNotifierImp.EXTRA_FOOD_ID) ?: return

        when (intent.action) {
            AlgidyNotifierImp.ACTION_CONSUME -> {
                scope.launch {
                    updateFoodStatusUseCase(foodId, status = FoodStatus.CONSUMED)
                }
            }

            AlgidyNotifierImp.ACTION_WASTE -> {
                scope.launch {
                    updateFoodStatusUseCase(foodId, status = FoodStatus.WASTED)
                }
            }
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(foodId.hashCode())
    }
}
