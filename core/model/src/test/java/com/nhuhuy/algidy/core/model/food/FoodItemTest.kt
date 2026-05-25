package com.nhuhuy.algidy.core.model.food

import org.junit.Assert.assertEquals
import org.junit.Test

class FoodItemTest {

    @Test
    fun `getFreshnessStatus should return EXPIRED when expiryDate is in the past`() {
        val past = System.currentTimeMillis() - (24 * 60 * 60 * 1000L) // 1 day ago
        val item = FoodItem(expiryDate = past)
        assertEquals(Freshness.EXPIRED, item.getFreshnessStatus())
    }

    @Test
    fun `getFreshnessStatus should return URGENT when expiryDate is within 3 days`() {
        val threeDaysInMs = 3 * 24 * 60 * 60 * 1000L
        val nearFuture = System.currentTimeMillis() + threeDaysInMs - 1000L
        val item = FoodItem(expiryDate = nearFuture)
        assertEquals(Freshness.URGENT, item.getFreshnessStatus())
    }

    @Test
    fun `getFreshnessStatus should return WARNING when expiryDate is within 7 days`() {
        val sevenDaysInMs = 7 * 24 * 60 * 60 * 1000L
        val nearFuture = System.currentTimeMillis() + sevenDaysInMs - 1000L
        val item = FoodItem(expiryDate = nearFuture)
        assertEquals(Freshness.WARNING, item.getFreshnessStatus())
    }

    @Test
    fun `getFreshnessStatus should return FRESH when expiryDate is beyond 7 days`() {
        val tenDaysInMs = 10 * 24 * 60 * 60 * 1000L
        val farFuture = System.currentTimeMillis() + tenDaysInMs
        val item = FoodItem(expiryDate = farFuture)
        assertEquals(Freshness.FRESH, item.getFreshnessStatus())
    }

    @Test
    fun `calculateFreshnessProgress should return 0 when current time is after expiry`() {
        val past = System.currentTimeMillis() - 1000L
        val purchase = past - 10000L
        val item = FoodItem(purchaseDate = purchase, expiryDate = past)
        assertEquals(0f, item.calculateFreshnessProgress())
    }

    @Test
    fun `calculateFreshnessProgress should return 1 when current time is before purchase`() {
        val future = System.currentTimeMillis() + 10000L
        val expiry = future + 10000L
        val item = FoodItem(purchaseDate = future, expiryDate = expiry)
        assertEquals(1f, item.calculateFreshnessProgress())
    }

    @Test
    fun `calculateFreshnessProgress should return 0_5 when current time is exactly in the middle`() {
        val now = System.currentTimeMillis()
        val duration = 100000L
        val purchase = now - (duration / 2)
        val expiry = now + (duration / 2)
        val item = FoodItem(purchaseDate = purchase, expiryDate = expiry)
        assertEquals(0.5f, item.calculateFreshnessProgress(), 0.01f)
    }

    @Test
    fun `getRemainingDays should return -1 when expiryDate is -1`() {
        val item = FoodItem(expiryDate = -1L)
        assertEquals(-1, item.getRemainingDays())
    }

    @Test
    fun `getRemainingDays should return correct days`() {
        val twoDaysInMs = 2 * 24 * 60 * 60 * 1000L
        val future = System.currentTimeMillis() + twoDaysInMs + 1000L // Add a buffer
        val item = FoodItem(expiryDate = future)
        assertEquals(2, item.getRemainingDays())
    }
}
