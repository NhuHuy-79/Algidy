package com.nhuhuy.algidy.core.model.validate

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class FoodValidatorTest {

    @Test
    fun `validateName should return EMPTY_FIELD when name is blank`() {
        val result = FoodValidator.validateName("  ")
        assertEquals(ValidationResult.EMPTY_FIELD, result)
    }

    @Test
    fun `validateName should return TEXT_TOO_SHORT when name is less than 2 chars`() {
        val result = FoodValidator.validateName("A")
        assertEquals(ValidationResult.TEXT_TOO_SHORT, result)
    }

    @Test
    fun `validateName should return SUCCESS when name is valid`() {
        val result = FoodValidator.validateName("Apple")
        assertEquals(ValidationResult.SUCCESS, result)
    }

    @Test
    fun `validateQuantity should return NEGATIVE_VALUE when quantity is less than 0`() {
        val result = FoodValidator.validateQuantity(-1.0)
        assertEquals(ValidationResult.NEGATIVE_VALUE, result)
    }

    @Test
    fun `validateQuantity should return SUCCESS when quantity is 0 or more`() {
        assertEquals(ValidationResult.SUCCESS, FoodValidator.validateQuantity(0.0))
        assertEquals(ValidationResult.SUCCESS, FoodValidator.validateQuantity(10.5))
    }

    @Test
    fun `validatePurchaseDate should return FUTURE_DATE when date is tomorrow`() {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis
        val result = FoodValidator.validatePurchaseDate(tomorrow)
        assertEquals(ValidationResult.FUTURE_DATE, result)
    }

    @Test
    fun `validateExpiryDate should return INVALID_DATE_RANGE when expiry is before purchase`() {
        val today = System.currentTimeMillis()
        val yesterday = today - (24 * 60 * 60 * 1000)

        val result = FoodValidator.validateExpiryDate(expiryDate = yesterday, purchaseDate = today)
        assertEquals(ValidationResult.INVALID_DATE_RANGE, result)
    }

    @Test
    fun `validateExpiryDate should return PAST_DATE when expiry is yesterday`() {
        val yesterday = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        val result = FoodValidator.validateExpiryDate(expiryDate = yesterday)
        assertEquals(ValidationResult.PAST_DATE, result)
    }
}
