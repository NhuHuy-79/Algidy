package com.nhuhuy.algidy.feature.inventory.domain.usecase

import app.cash.turbine.test
import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InventoryUseCasesTest {

    private lateinit var foodRepository: FoodRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var localMediaStorage: LocalMediaStorage

    @Before
    fun setUp() {
        foodRepository = mockk()
        searchRepository = mockk()
        localMediaStorage = mockk()
    }

    @Test
    fun `CreateFoodItemUseCase should call addFoodItem on repository`() = runTest {
        val useCase = CreateFoodItemUseCase(foodRepository, localMediaStorage)
        val foodItem = FoodItem(name = "Test")
        coEvery { foodRepository.addFoodItem(foodItem) } returns Resource.Success(foodItem)

        useCase(foodItem)

        coVerify(exactly = 1) { foodRepository.addFoodItem(foodItem) }
    }

    @Test
    fun `ObserveFoodItemUseCase should return flow from repository`() = runTest {
        val useCase = ObserveFoodItemUseCase(foodRepository)
        val items = listOf(FoodItem(name = "Test"))
        every { foodRepository.observeAllActiveFoodItems() } returns flowOf(items)

        useCase().test {
            assertEquals(items, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GetHistoryResultUseCase should return list from searchRepository`() = runTest {
        val useCase = GetHistoryResultUseCase(searchRepository)
        val history = listOf(HistoryResult(name = "query", timeStamp = 1L))
        coEvery { searchRepository.getHistoryResultList() } returns history

        val result = useCase()

        assertEquals(history, result)
    }
}

