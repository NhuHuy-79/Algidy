package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.DeleteFoodItemUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteFoodItemUseCaseTest {

    private lateinit var foodRepository: FoodRepository
    private lateinit var deleteFoodItemUseCase: DeleteFoodItemUseCase

    @Before
    fun setUp() {
        foodRepository = mockk()
        deleteFoodItemUseCase = DeleteFoodItemUseCase(foodRepository)
    }

    @Test
    fun `invoke should call removeFoodItem on repository`() = runTest {
        // Given
        val foodId = "test_id"
        coEvery { foodRepository.removeFoodItem(foodId) } returns Unit

        // When
        deleteFoodItemUseCase(foodId)

        // Then
        coVerify(exactly = 1) { foodRepository.removeFoodItem(foodId) }
    }
}
