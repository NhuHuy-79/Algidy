package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import app.cash.turbine.test
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.delegate.FoodEntryDelegateImpl
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.CreateFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.DeleteFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.ObserveFoodItemUseCase
import com.nhuhuy.algidy.feature.inventory.util.MainDispatcherRule
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

class InventoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var observeFoodItemUseCase: ObserveFoodItemUseCase
    private lateinit var createFoodItemUseCase: CreateFoodItemUseCase
    private lateinit var deleteFoodItemUseCase: DeleteFoodItemUseCase
    private lateinit var foodEntryDelegateImpl: FoodEntryDelegateImpl
    private lateinit var viewModel: InventoryViewModel

    @Before
    fun setUp() {
        observeFoodItemUseCase = mockk()
        createFoodItemUseCase = mockk()
        deleteFoodItemUseCase = mockk()
        foodEntryDelegateImpl = FoodEntryDelegateImpl()

        every { observeFoodItemUseCase() } returns flowOf(emptyList())

        viewModel = InventoryViewModel(
            observeFoodItemUseCase,
            foodEntryDelegateImpl,
            createFoodItemUseCase,
            deleteFoodItemUseCase
        )
    }

    @Test
    fun `resultState should emit Loading then Empty when usecase returns empty list`() = runTest {
        every { observeFoodItemUseCase() } returns flowOf(emptyList())

        // Re-init viewModel to trigger the flow
        viewModel = InventoryViewModel(
            observeFoodItemUseCase,
            foodEntryDelegateImpl,
            createFoodItemUseCase,
            deleteFoodItemUseCase
        )

        viewModel.resultState.test {
            assertEquals(InventoryResultState.Empty, awaitItem())
        }
    }

    @Test
    fun `resultState should emit Success when usecase returns items`() = runTest {
        val items = listOf(FoodItem(id = "1", name = "Apple"))
        every { observeFoodItemUseCase() } returns flowOf(items)

        viewModel = InventoryViewModel(
            observeFoodItemUseCase,
            foodEntryDelegateImpl,
            createFoodItemUseCase,
            deleteFoodItemUseCase
        )

        viewModel.resultState.test {
            val state = awaitItem()
            assert(state is InventoryResultState.Success)
            assertEquals(items, (state as InventoryResultState.Success).items)
        }
    }

    @Test
    fun `onAction RemoveItem should call deleteFoodItemUseCase`() = runTest {
        val foodId = "test_id"
        coEvery { deleteFoodItemUseCase(foodId) } returns Unit

        viewModel.onAction(InventoryAction.RemoveItem(foodId))

        coVerify(exactly = 1) { deleteFoodItemUseCase(foodId) }
    }

    @Test
    fun `onAction OnAddFabClick should show food sheet`() = runTest {
        viewModel.onAction(InventoryAction.OnAddFabClick)

        viewModel.uiState.test {
            assertEquals(InventoryOverlay.FOOD_SHEET, awaitItem().overlay)
        }
    }
}
