package com.nhuhuy.algidy.core.notifications.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.net.toUri
import com.nhuhuy.algidy.core.notifications.domain.AlgidyNotifier
import com.nhuhuy.algidy.core.notifications.domain.NotificationFoodItem

class AlgidyNotifierImp(
    private val context: Context,
    private val notificationFactory: AlgidyNotificationFactory,
) : AlgidyNotifier {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        private const val EXPIRY_ALERT_ID = 1001
        private const val WEEKLY_SUMMARY_ID = 1002
        const val ACTION_CONSUME = "com.nhuhuy.algidy.ACTION_CONSUME"
        const val ACTION_WASTE = "com.nhuhuy.algidy.ACTION_WASTE"
        const val EXTRA_FOOD_ID = "EXTRA_FOOD_ID"
    }

    override fun showExpiringItemsAlert(items: List<NotificationFoodItem>) {
        if (items.isEmpty()) return
        val intent = Intent(Intent.ACTION_VIEW, "algidy://home".toUri()).apply {
            setPackage(context.packageName) // Đảm bảo chỉ app Algidy mới mở được intent này
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = notificationFactory.createExpiringItemsAlert(
            items = items,
            pendingIntent = pendingIntent
        )

        notificationManager.notify(EXPIRY_ALERT_ID, notification)
    }

    override fun showActionableExpiryPrompt(
        foodId: String,
        foodName: String,
        image: Bitmap?
    ) {
        val mainIntent = Intent(Intent.ACTION_VIEW, "algidy://food/$foodId".toUri()).apply {
            setPackage(context.packageName)
        }
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            foodId.hashCode(), // Dùng hashcode để tạo request code duy nhất
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val consumeIntent = Intent(ACTION_CONSUME).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_FOOD_ID, foodId)
        }
        val consumePendingIntent = PendingIntent.getBroadcast(
            context,
            foodId.hashCode() + 1,
            consumeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val wasteIntent = Intent(ACTION_WASTE).apply {
            setPackage(context.packageName)
            putExtra(EXTRA_FOOD_ID, foodId)
        }
        val wastePendingIntent = PendingIntent.getBroadcast(
            context,
            foodId.hashCode() + 2,
            wasteIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = notificationFactory.createActionableExpiryPrompt(
            foodName = foodName,
            mainIntent = mainPendingIntent,
            consumeIntent = consumePendingIntent,
            wasteIntent = wastePendingIntent
        )

        notificationManager.notify(foodId.hashCode(), notification)
    }

    override fun showWeeklySummary(consumedCount: Int, wastedCount: Int) {
        val intent = Intent(Intent.ACTION_VIEW, "algidy://report".toUri()).apply {
            setPackage(context.packageName)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = notificationFactory.createWeeklySummary(
            consumedCount = consumedCount,
            wastedCount = wastedCount,
            pendingIntent = pendingIntent
        )

        notificationManager.notify(WEEKLY_SUMMARY_ID, notification)
    }

    override fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}