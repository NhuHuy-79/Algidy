package com.nhuhuy.algidy.core.notifications.domain

interface AlgidyNotifier {

    /**
     * Gửi cảnh báo danh sách thực phẩm sắp hỏng (Sử dụng InboxStyle).
     */
    fun showExpiringItemsAlert(items: List<NotificationFoodItem>)

    /**
     * Gửi thông báo kèm Nút Hành Động (Action Buttons) để người dùng thao tác nhanh.
     */
    suspend fun showActionableExpiryPrompt(foodId: String, foodName: String, uriPath: String?)

    /**
     * Gửi báo cáo tổng kết tuần (Im lặng, mức độ ưu tiên thấp).
     */
    fun showWeeklySummary(consumedCount: Int, wastedCount: Int)

    /**
     * Xóa một thông báo cụ thể (Ví dụ: khi người dùng đã mở app và xử lý món ăn đó).
     */
    fun cancelNotification(notificationId: Int)
}

// Data class nội bộ của module notifications để nhận dữ liệu từ các module khác
data class NotificationFoodItem(
    val id: String,
    val name: String,
    val daysLeft: Int
)