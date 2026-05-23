package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSource
import com.nhuhuy.algidy.toGenericNormalized
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FoodRepositoryImplTest {

    private lateinit var foodDao: FoodDao
    private lateinit var foodRemoteDataSource: FoodRemoteDataSource
    private lateinit var appDispatchers: AppDispatchers
    private lateinit var repository: FoodRepositoryImpl

    @Before
    fun setUp() {
        mockkStatic("com.nhuhuy.algidy.StringExtKt")
        every { any<String>().toGenericNormalized() } answers { firstArg() }

        foodDao = mockk()
        foodRemoteDataSource = mockk()
        appDispatchers = object : AppDispatchers {
            override val io = Dispatchers.Unconfined
            override val main = Dispatchers.Unconfined
        }
        repository = FoodRepositoryImpl(appDispatchers, foodDao, foodRemoteDataSource)
    }

    @org.junit.After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getFoodById should call dao and return domain model`() = runTest {
        val foodId = "1"
        val entity = FoodItemEntity(
            id = foodId,
            name = "Milk",
            normalizedName = "milk",
            categoryId = "dairy",
            location = StorageLocation.FRIDGE,
            quantity = 1.0,
            itemUnit = ItemUnit.LITER,
            purchaseDate = 1000L,
            expiryDate = 2000L,
            imageUri = "uri",
            isFavorite = false,
            notes = "",
            category = DefaultFoodCategory.DAIRY_EGGS
        )
        coEvery { foodDao.getFoodById(foodId) } returns entity

        val result = repository.getFoodById(foodId)

        assertEquals("Milk", result?.name)
        coVerify(exactly = 1) { foodDao.getFoodById(foodId) }
    }

    @Test
    fun `addFoodItem should call dao insert`() = runTest {
        val foodItem = FoodItem(id = "1", name = "Apple")
        coEvery { foodDao.insertFood(any()) } returns Unit

        repository.addFoodItem(foodItem)

        coVerify(exactly = 1) { foodDao.insertFood(any()) }
    }
}
