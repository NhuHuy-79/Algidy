package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import app.cash.turbine.test
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegateImpl
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsConsumedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.MarkFoodAsWastedUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.ObserveFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.domain.usecase.UpdateFoodDetailUseCase
import com.nhuhuy.algidy.feature.detail.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var markFoodAsConsumedUseCase: MarkFoodAsConsumedUseCase
    private lateinit var markFoodAsWastedUseCase: MarkFoodAsWastedUseCase
    private lateinit var updateFoodDetailUseCase: UpdateFoodDetailUseCase
    private lateinit var observeFoodDetailUseCase: ObserveFoodDetailUseCase
    private lateinit var foodEntryDelegateImpl: FoodEntryDelegateImpl
    private lateinit var viewModel: DetailViewModel

    private val foodItemId = "test_id"

    @Before
    fun setUp() {
        markFoodAsConsumedUseCase = mockk()
        markFoodAsWastedUseCase = mockk()
        updateFoodDetailUseCase = mockk()
        observeFoodDetailUseCase = mockk()
        foodEntryDelegateImpl = FoodEntryDelegateImpl()

        every { observeFoodDetailUseCase(foodItemId) } returns flowOf(FoodItem(id = foodItemId))

        viewModel = DetailViewModel(
            foodItemId,
            markFoodAsConsumedUseCase,
            markFoodAsWastedUseCase,
            updateFoodDetailUseCase,
            observeFoodDetailUseCase,
            foodEntryDelegateImpl
        )
    }

    @Test
    fun `init should observe food detail and update uiState`() = runTest {
        val foodItem = FoodItem(id = foodItemId, name = "Banana")
        every { observeFoodDetailUseCase(foodItemId) } returns flowOf(foodItem)

        // Re-init to trigger init block
        viewModel = DetailViewModel(
            foodItemId,
            markFoodAsConsumedUseCase,
            markFoodAsWastedUseCase,
            updateFoodDetailUseCase,
            observeFoodDetailUseCase,
            foodEntryDelegateImpl
        )

        viewModel.uiState.test {
            assertEquals(foodItem, awaitItem().detailFoodItem)
        }
    }

    @Test
    fun `onAction OnConsumeItem should call markFoodAsConsumedUseCase`() = runTest {
        coEvery { markFoodAsConsumedUseCase(foodItemId) } returns Unit

        viewModel.onAction(DetailAction.OnConsumeItem)

        coVerify(exactly = 1) { markFoodAsConsumedUseCase(foodItemId) }
        viewModel.uiState.test {
            assertEquals(DetailOverlay.None, awaitItem().actionState)
        }
    }

    @Test
    fun `onAction OnWastedItem should call markFoodAsWastedUseCase`() = runTest {
        coEvery { markFoodAsWastedUseCase(foodItemId) } returns Unit

        viewModel.onAction(DetailAction.OnWastedItem)

        coVerify(exactly = 1) { markFoodAsWastedUseCase(foodItemId) }
        viewModel.uiState.test {
            assertEquals(DetailOverlay.None, awaitItem().actionState)
        }
    }
}
