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
import timber.log.Timber


class AlgidyNotificationFactory(
    private val imageLoader: ImageLoader,
    private val context: Context
) {

    suspend fun createExpiringItemsAlert(
        items: List<NotificationFoodItem>,
        pendingIntent: PendingIntent
    ): Notification {

        val sortedItems = items.sortedBy { it.daysLeft }

        val title = context.resources.getQuantityString(
            R.plurals.notif_expiry_multiple_title,
            sortedItems.size,
            sortedItems.size
        )

        val content = context.getString(
            R.string.notif_expiry_review_before_expire
        )

        val nextExpiry = sortedItems.firstOrNull()

        val summaryText = nextExpiry?.let {
            when (it.daysLeft) {
                1 -> context.getString(R.string.notif_expiry_next_item_one_day, it.name)

                else -> context.getString(
                    R.string.notif_expiry_next_item_days,
                    it.name,
                    it.daysLeft
                )
            }
        }

        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle(title)

        summaryText?.let {
            inboxStyle.setSummaryText(it)
        }

        sortedItems.take(5).forEach { food ->
            val line = when (food.daysLeft) {
                1 -> context.getString(R.string.notif_expiry_item_one_day, food.name)
                else -> context.getString(R.string.notif_expiry_item_days, food.name, food.daysLeft)
            }

            inboxStyle.addLine(line)
        }

        if (sortedItems.size > 5) {
            inboxStyle.addLine(
                context.getString(
                    R.string.notif_expiry_overflow,
                    sortedItems.size - 5
                )
            )
        }

        val bitmap = items.map { it.imageUri }.find { it != null }?.let {
            loadBitmap(
                context = context,
                uri = it.toUri()
            )
        }

        return NotificationCompat.Builder(
            context,
            NotificationChannelManager.CHANNEL_ALERT_ID
        )
            .setSmallIcon(
                com.nhuhuy.algidy.core.notifications.R.drawable.salad
            )
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(inboxStyle)
            .setLargeIcon(bitmap)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                com.nhuhuy.algidy.core.designsystem.R.drawable.ic_grocery,
                context.getString(R.string.review_foods),
                pendingIntent
            )
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
                com.nhuhuy.algidy.core.designsystem.R.drawable.ic_delete,
                actionWasted,
                wasteIntent
            )
            .setStyle(
                bitmap?.let {
                    NotificationCompat.BigTextStyle()
                        .setBigContentTitle(message)
                }
            )
            .setLargeIcon(bitmap)
            .setAutoCancel(true)
            .build()
    }

    suspend fun loadBitmap(
        context: Context,
        uri: Uri
    ): Bitmap? {
        return try {
            val request = ImageRequest.Builder(context)
                .data(uri)
                .allowHardware(false)
                .size(256, 256)
                .build()

            val result = imageLoader.execute(request)

            (result as? SuccessResult)
                ?.image
                ?.asDrawable(context.resources)
                ?.toBitmap()

        } catch (e: Exception) {
            Timber.w(e, "Failed to load notification image: $uri")
            null
        }
    }
}