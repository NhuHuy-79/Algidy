package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DetailUseCasesTest {

    private lateinit var foodRepository: FoodRepository
    private lateinit var localMediaStorage: LocalMediaStorage

    @Before
    fun setUp() {
        foodRepository = mockk()
        localMediaStorage = mockk()
    }

    @Test
    fun `GetFoodDetailUseCase should call getFoodById on repository`() = runTest {
        val useCase = GetFoodDetailUseCase(foodRepository)
        val foodId = "1"
        val foodItem = FoodItem(id = foodId)
        coEvery { foodRepository.getFoodById(foodId) } returns foodItem

        val result = useCase(foodId)

        assertEquals(foodItem, result)
        coVerify(exactly = 1) { foodRepository.getFoodById(foodId) }
    }

    @Test
    fun `UpdateFoodDetailUseCase should call updateFoodItem on repository`() = runTest {
        val useCase = UpdateFoodDetailUseCase(foodRepository, localMediaStorage)
        val foodItem = FoodItem(id = "1", name = "Updated")
        coEvery { foodRepository.updateFoodItem(any()) } returns Resource.Success(Unit)

        useCase(foodItem, null)

        coVerify(exactly = 1) { foodRepository.updateFoodItem(any()) }
    }
}
