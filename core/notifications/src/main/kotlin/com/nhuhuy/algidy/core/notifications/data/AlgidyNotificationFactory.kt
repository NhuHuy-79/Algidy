package com.nhuhuy.algidy.core.notifications.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import com.nhuhuy.algidy.core.notifications.domain.NotificationFoodItem
import com.nhuhuy.algidy.core.presentation.R


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
            .setSmallIcon(com.nhuhuy.algidy.core.notifications.R.drawable.salad)
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
            .setSmallIcon(com.nhuhuy.algidy.core.notifications.R.drawable.salad)
            .setContentTitle(title)
            .setContentText(shortMessage)
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    suspend fun createActionableExpiryPrompt(
        uriPath: String? = null,
        foodName: String,
        mainIntent: PendingIntent,
        consumeIntent: PendingIntent,
        wasteIntent: PendingIntent
    ): Notification {
        val title = context.getString(R.string.notif_action_prompt_title, foodName)
        val message = context.getString(R.string.notif_action_prompt_message)
        val actionConsumed = context.getString(R.string.notif_action_consumed)
        val actionWasted = context.getString(R.string.notif_action_wasted)
        val bitmap = uriPath?.let {
            loadBitmap(
                context = context,
                uri = it.toUri()
            )
        }

        return NotificationCompat.Builder(context, NotificationChannelManager.CHANNEL_ALERT_ID)
            .setSmallIcon(com.nhuhuy.algidy.core.notifications.R.drawable.salad)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(mainIntent)
            // Nút "Đã dùng"
            .addAction(
                com.nhuhuy.algidy.core.designsystem.R.drawable.ic_check_small,
                actionConsumed,
                consumeIntent
            )
            // Nút "Vứt bỏ"
            .addAction(
                R.drawable.ic_delete,
                actionWasted,
                wasteIntent
            )
            .setLargeIcon(bitmap)
            .setAutoCancel(true)
            .build()
    }

    suspend fun loadBitmap(
        context: Context,
        uri: Uri
    ): Bitmap? {
        val loader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)

        if (result is SuccessResult) {
            val bitmap = result.image
                .asDrawable(context.resources)
                .toBitmap()

            return bitmap
        } else return null
    }
}