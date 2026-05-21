package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchFoodUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchFoodUseCase: SearchFoodUseCase

    @Before
    fun setUp() {
        searchRepository = mockk()
        searchFoodUseCase = SearchFoodUseCase(searchRepository)
    }

    @Test
    fun `invoke should call searchRepository and add to history`() = runTest {
        val query = "Milk"
        val mockItems = listOf(FoodItem(name = "Fresh Milk"))

        coEvery { searchRepository.getFoodItemListByQuery(any()) } returns mockItems
        coEvery { searchRepository.addHistoryResult(any()) } returns Unit

        val result = searchFoodUseCase(query)

        assertEquals(mockItems, result)
        coVerify(exactly = 1) { searchRepository.getFoodItemListByQuery(any()) }
        coVerify(exactly = 1) { searchRepository.addHistoryResult(any()) }
    }
}
