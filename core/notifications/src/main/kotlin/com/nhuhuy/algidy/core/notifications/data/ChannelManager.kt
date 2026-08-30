package com.nhuhuy.algidy.core.notifications.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.nhuhuy.algidy.core.presentation.R

object NotificationChannelManager {
    const val CHANNEL_ALERT_ID = "alert_channel"
    const val CHANNEL_REPORT_ID = "report_channel"

    const val GENERAL_CHANNEL = "general_channel"

    fun createAllChannels(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Channel cho cảnh báo hết hạn (High importance)
        val alertChannel = NotificationChannel(
            CHANNEL_ALERT_ID,
            context.getString(R.string.notif_expiry_summary),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notif_expiry_content)
        }

        // Channel cho báo cáo hàng tuần (Low importance)
        val reportChannel = NotificationChannel(
            CHANNEL_REPORT_ID,
            context.getString(R.string.notif_weekly_title),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notif_weekly_short, 0, 0)
        }

        val generalChannel = NotificationChannel(
            GENERAL_CHANNEL,
            context.getString(R.string.notif_general_title),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notif_general_content)
        }


        notificationManager.createNotificationChannels(
            listOf(
                alertChannel,
                reportChannel,
                generalChannel
            )
        )
    }
}
