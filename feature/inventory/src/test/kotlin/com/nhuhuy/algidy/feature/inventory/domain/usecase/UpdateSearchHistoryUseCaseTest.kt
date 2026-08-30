package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.UpdateSearchHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateSearchHistoryUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var updateSearchHistoryUseCase: UpdateSearchHistoryUseCase

    @Before
    fun setUp() {
        searchRepository = mockk()
        updateSearchHistoryUseCase = UpdateSearchHistoryUseCase(searchRepository)
    }

    @Test
    fun `invoke should call searchRepository and add to history`() = runTest {
        val query = "Milk"
        val mockItems = listOf(FoodItem(name = "Fresh Milk"))

        coEvery { searchRepository.getFoodItemListByQuery(any()) } returns mockItems
        coEvery { searchRepository.addHistoryResult(any()) } returns Unit

        val result = updateSearchHistoryUseCase(query)

        assertEquals(mockItems, result)
        coVerify(exactly = 1) { searchRepository.getFoodItemListByQuery(any()) }
        coVerify(exactly = 1) { searchRepository.addHistoryResult(any()) }
    }
}
