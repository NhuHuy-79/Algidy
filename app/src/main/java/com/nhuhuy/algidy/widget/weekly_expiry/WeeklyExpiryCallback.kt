package com.nhuhuy.algidy.widget.weekly_expiry

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.nhuhuy.algidy.widget.worker.CallbackScheduler

class RefreshWeeklyExpiryWidget : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WeeklyExpiryWidget().update(context = context, id = glanceId)
    }
}

class WasteAllFoodsCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        CallbackScheduler(context).scheduleWasteWeeklyAllFoodsWorker()
    }
}

class ConsumeFoodCallBack : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val foodId = parameters[foodIdKey]
        foodId?.let {
            CallbackScheduler(context).scheduleConsumeFoodWorker(foodId = foodId)
        }
    }
}
