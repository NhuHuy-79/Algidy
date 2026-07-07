package com.nhuhuy.algidy.widget.weekly_progress

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class RefreshWeeklyFreshnessWiget : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WeeklyFreshnessWidget().update(context, glanceId)
    }
}