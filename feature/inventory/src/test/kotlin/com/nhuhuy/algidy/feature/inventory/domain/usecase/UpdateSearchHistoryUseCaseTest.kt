package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.UpdateSearchHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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

        coEvery { searchRepository.addHistoryResult(any()) } returns Unit

        updateSearchHistoryUseCase(query)

        coVerify(exactly = 1) { searchRepository.addHistoryResult(any()) }
    }
}
