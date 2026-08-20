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

    fun validatePurchaseDate(
        purchaseDate: Long
    ): ValidationResult {
        if (purchaseDate == -1L) {
            return ValidationResult.EMPTY_FIELD
        }
        val today = getStartOfDay(System.currentTimeMillis())

        return if (purchaseDate > today) {
            ValidationResult.FUTURE_DATE
        } else {
            ValidationResult.SUCCESS
        }
    }

    fun validateExpiryDate(
        expiryDate: Long,
        purchaseDate: Long = -1L
    ): ValidationResult {
        if (expiryDate == -1L) {
            return ValidationResult.EMPTY_FIELD
        }

        if (purchaseDate != -1L && expiryDate < purchaseDate) {
            return ValidationResult.INVALID_DATE_RANGE
        }

        val today = getStartOfDay(System.currentTimeMillis())

        return if (expiryDate < today) {
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
    INVALID_DATE_RANGE;

    companion object {
        fun ValidationResult.isValid(): Boolean {
            return this == SUCCESS || this == IDLE
        }
    }
}
