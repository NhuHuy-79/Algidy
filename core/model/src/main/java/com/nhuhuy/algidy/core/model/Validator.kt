package com.nhuhuy.algidy.core.model

object FoodValidator {

    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.EMPTY_FIELD
            name.length < 2 -> ValidationResult.TEXT_TOO_SHORT
            else -> ValidationResult.SUCCESS
        }
    }

    fun validateQuantity(quantity: Double): ValidationResult {
        return when {
            quantity < 0 -> ValidationResult.NEGATIVE_VALUE
            else -> ValidationResult.SUCCESS
        }
    }

    fun validateExpiryDate(expiryDate: Long): ValidationResult {
        return if (expiryDate < System.currentTimeMillis()) {
            ValidationResult.PAST_DATE
        } else {
            ValidationResult.SUCCESS
        }
    }
}


enum class ValidationResult {
    SUCCESS,
    EMPTY_FIELD,
    INVALID_NUMBER,
    NEGATIVE_VALUE,
    PAST_DATE,
    TEXT_TOO_SHORT
}