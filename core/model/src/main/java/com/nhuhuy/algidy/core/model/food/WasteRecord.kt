package com.nhuhuy.algidy.core.model.food

data class WasteRecord(
    val id: String,
    val foodName: String,
    val amount: Double,
    val unit: String,
    val reason: WasteReason,
    val date: Long = System.currentTimeMillis()
)

enum class WasteReason {
    EXPIRED,
    SPOILED,
    EATEN,
    GIVEN_AWAY
}