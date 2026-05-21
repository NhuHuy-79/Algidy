package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MarkFoodAsWastedUseCaseTest {

    private lateinit var foodRepository: FoodRepository
    private lateinit var markFoodAsWastedUseCase: MarkFoodAsWastedUseCase

    @Before
    fun setUp() {
        foodRepository = mockk()
        markFoodAsWastedUseCase = MarkFoodAsWastedUseCase(foodRepository)
    }

    @Test
    fun `invoke should call updateFoodStatus with WASTED status`() = runTest {
        // Given
        val foodId = "test_id"
        coEvery {
            foodRepository.updateFoodStatus(
                foodId,
                FoodStatus.WASTED
            )
        } returns Resource.Success(foodId)

        // When
        markFoodAsWastedUseCase(foodId)

        // Then
        coVerify(exactly = 1) { foodRepository.updateFoodStatus(foodId, FoodStatus.WASTED) }
    }
}
