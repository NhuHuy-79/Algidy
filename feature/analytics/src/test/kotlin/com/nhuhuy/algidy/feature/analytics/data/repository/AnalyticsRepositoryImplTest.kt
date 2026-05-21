package com.nhuhuy.algidy.feature.analytics.data.repository

import app.cash.turbine.test
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class AnalyticsRepositoryImplTest {

    private lateinit var foodRepository: FoodRepository
    private lateinit var analyticsRepository: AnalyticsRepositoryImpl

    @Before
    fun setUp() {
        foodRepository = mockk()
        analyticsRepository = AnalyticsRepositoryImpl(foodRepository)
    }

    @Test
    fun `getSummaryStats should correctly count items by status`() = runTest {
        val now = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val items = listOf(
            FoodItem(id = "1", status = FoodStatus.ACTIVE),
            FoodItem(id = "2", status = FoodStatus.WASTED, resolvedDate = now),
            FoodItem(id = "3", status = FoodStatus.CONSUMED, resolvedDate = now)
        )
        every { foodRepository.observeAllFoodItems() } returns flowOf(items)

        analyticsRepository.getSummaryStats().test {
            val stats = awaitItem()
            assertEquals(1, stats.wastedCount)
            assertEquals(1, stats.consumedCount)
            assertEquals(1, stats.otherCount)
            awaitComplete()
        }
    }
}
