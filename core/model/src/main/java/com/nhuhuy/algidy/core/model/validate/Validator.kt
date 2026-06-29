package com.nhuhuy.algidy.core.model.validate

import java.util.Calendar

object FoodValidator {

    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.EMPTY_FIELD
            name.length < 2 -> ValidationResult.TEXT_TOO_SHORT
            else -> ValidationResult.SUCCESS
        }
    }

    fun validatePurchaseDate(purchaseDate: Long): ValidationResult {
        if (purchaseDate == -1L) return ValidationResult.EMPTY_FIELD
        val todayEnd = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        return if (purchaseDate > todayEnd) {
            ValidationResult.FUTURE_DATE
        } else {
            ValidationResult.SUCCESS
        }
    }

    fun validateExpiryDate(expiryDate: Long, purchaseDate: Long = -1L): ValidationResult {
        if (expiryDate == -1L) return ValidationResult.EMPTY_FIELD
        if (purchaseDate != -1L) {
            val purchaseStartOfDay = getStartOfDay(purchaseDate)
            val expiryStartOfDay = getStartOfDay(expiryDate)

            if (expiryStartOfDay < purchaseStartOfDay) {
                return ValidationResult.INVALID_DATE_RANGE
            }
        }
        val todayStart = getStartOfDay(System.currentTimeMillis())
        return if (expiryDate < todayStart) {
            ValidationResult.PAST_DATE
        } else {
            ValidationResult.SUCCESS
        }
    }

    private fun getStartOfDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}


enum class ValidationResult {
    SUCCESS,
    IDLE,
    EMPTY_FIELD,
    INVALID_NUMBER,
    NEGATIVE_VALUE,
    PAST_DATE,
    TEXT_TOO_SHORT,
    FUTURE_DATE,
    INVALID_DATE_RANGE
}
