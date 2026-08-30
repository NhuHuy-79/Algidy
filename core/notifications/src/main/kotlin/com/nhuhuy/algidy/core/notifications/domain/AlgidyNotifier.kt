package com.nhuhuy.algidy.core.notifications.domain

interface AlgidyNotifier {
    suspend fun showNewUpdateVersion()
    suspend fun showExpiringItemsAlert(items: List<NotificationFoodItem>)
    suspend fun showActionableExpiryPrompt(foodId: String, foodName: String, uriPath: String?)
    fun showWeeklySummary(consumedCount: Int, wastedCount: Int)
    fun cancelNotification(notificationId: Int)
}

data class NotificationFoodItem(
    val id: String,
    val name: String,
    val daysLeft: Int,
    val imageUri: String? = null,
)