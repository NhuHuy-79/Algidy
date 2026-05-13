package com.nhuhuy.algidy.core.notifications.alarm

interface ExpiryAlarmer {
    fun scheduleAlarm(itemId: Long, triggerAtMillis: Long)
    fun cancelAlarm(itemId: Long)
}
