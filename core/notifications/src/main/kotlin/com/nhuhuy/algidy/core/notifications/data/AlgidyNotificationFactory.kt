package com.nhuhuy.algidy.core.notifications.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.nhuhuy.algidy.core.notifications.R
import com.nhuhuy.algidy.core.notifications.domain.NotificationFoodItem


class AlgidyNotificationFactory(
    private val context: Context
) {

    fun createExpiringItemsAlert(
        items: List<NotificationFoodItem>,
        pendingIntent: PendingIntent
    ): Notification {
        val title = context.getString(R.string.notif_expiry_multiple_title, items.size)
        val summary = context.getString(R.string.notif_expiry_summary)
        val content = context.getString(R.string.notif_expiry_content)

        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle(title)
            .setSummaryText(summary)

        items.take(5).forEach { food ->
            val line = context.getString(R.string.notif_expiry_item_line, food.name, food.daysLeft)
            inboxStyle.addLine(line)
        }

        if (items.size > 5) {
            val overflowCount = items.size - 5
            val overflowText = context.getString(R.string.notif_expiry_overflow, overflowCount)
            inboxStyle.addLine(overflowText)
        }

        return NotificationCompat.Builder(context, NotificationChannelManager.CHANNEL_ALERT_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(inboxStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    fun createWeeklySummary(
        consumedCount: Int,
        wastedCount: Int,
        pendingIntent: PendingIntent
    ): Notification {
        val title = context.getString(R.string.notif_weekly_title)
        val shortMessage =
            context.getString(R.string.notif_weekly_short, consumedCount, wastedCount)
        val longDetail = context.getString(R.string.notif_weekly_detail, consumedCount, wastedCount)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .setBigContentTitle(title)
            .bigText(longDetail)

        return NotificationCompat.Builder(context, NotificationChannelManager.CHANNEL_REPORT_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(shortMessage)
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    fun createActionableExpiryPrompt(
        foodName: String,
        mainIntent: PendingIntent,
        consumeIntent: PendingIntent,
        wasteIntent: PendingIntent
    ): Notification {
        val title = context.getString(R.string.notif_action_prompt_title, foodName)
        val message = context.getString(R.string.notif_action_prompt_message)
        val actionConsumed = context.getString(R.string.notif_action_consumed)
        val actionWasted = context.getString(R.string.notif_action_wasted)

        return NotificationCompat.Builder(context, NotificationChannelManager.CHANNEL_ALERT_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(mainIntent)
            // Nút "Đã dùng"
            .addAction(
                android.R.drawable.ic_menu_edit,
                actionConsumed,
                consumeIntent
            )
            // Nút "Vứt bỏ"
            .addAction(
                android.R.drawable.ic_menu_delete,
                actionWasted,
                wasteIntent
            )
            .setAutoCancel(true)
            .build()
    }
}