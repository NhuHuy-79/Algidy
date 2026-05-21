package com.nhuhuy.algidy.feature.detail.domain.usecase

import app.cash.turbine.test
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveFoodDetailUseCaseTest {

    private lateinit var foodRepository: FoodRepository
    private lateinit var observeFoodDetailUseCase: ObserveFoodDetailUseCase

    @Before
    fun setUp() {
        foodRepository = mockk()
        observeFoodDetailUseCase = ObserveFoodDetailUseCase(foodRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val foodId = "test_id"
        val mockItem = FoodItem(id = foodId, name = "Apple")
        every { foodRepository.observeFoodItemById(foodId) } returns flowOf(mockItem)

        observeFoodDetailUseCase(foodId).test {
            assertEquals(mockItem, awaitItem())
            awaitComplete()
        }
    }
}
