package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.model.food.FoodCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.toGenericNormalized
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FoodMapperTest {

    @Before
    fun setUp() {
        mockkStatic("com.nhuhuy.algidy.StringExtKt")
        every { any<String>().toGenericNormalized() } answers { firstArg() }
    }

    @org.junit.After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `toDomain should correctly map entity to domain model`() {
        val entity = FoodItemEntity(
            id = "1",
            name = "Milk",
            normalizedName = "milk",
            categoryId = "dairy",
            location = StorageLocation.FRIDGE,
            quantity = 1.0,
            itemUnit = ItemUnit.LITER,
            purchaseDate = 1000L,
            expiryDate = 2000L,
            imageUri = "uri",
            isFavorite = true,
            notes = "note",
            category = FoodCategory.DAIRY_EGGS,
            status = FoodStatus.ACTIVE,
            resolvedDate = null
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.location, domain.location)
        assertEquals(entity.quantity, domain.quantity, 0.0)
        assertEquals(entity.itemUnit, domain.itemUnit)
        assertEquals(entity.purchaseDate, domain.purchaseDate)
        assertEquals(entity.expiryDate, domain.expiryDate)
        assertEquals(entity.imageUri, domain.imageUri)
        assertEquals(entity.isFavorite, domain.isFavorite)
        assertEquals(entity.notes, domain.notes)
        assertEquals(entity.category, domain.foodCategory)
        assertEquals(entity.status, domain.status)
    }

    @Test
    fun `toEntity should correctly map domain to entity model`() {
        val domain = FoodItem(
            id = "1",
            name = "Milk",
            categoryId = "dairy",
            location = StorageLocation.FRIDGE,
            quantity = 1.0,
            itemUnit = ItemUnit.LITER,
            purchaseDate = 1000L,
            expiryDate = 2000L,
            imageUri = "uri",
            isFavorite = true,
            notes = "note",
            foodCategory = FoodCategory.DAIRY_EGGS,
            status = FoodStatus.ACTIVE,
            resolvedDate = null
        )

        val entity = domain.toEntity()

        assertEquals(domain.id, entity.id)
        assertEquals(domain.name, entity.name)
        assertEquals(domain.location, entity.location)
        assertEquals(domain.quantity, entity.quantity, 0.0)
        assertEquals(domain.itemUnit, entity.itemUnit)
        assertEquals(domain.purchaseDate, entity.purchaseDate)
        assertEquals(domain.expiryDate, entity.expiryDate)
        assertEquals(domain.imageUri, entity.imageUri)
        assertEquals(domain.isFavorite, entity.isFavorite)
        assertEquals(domain.notes, entity.notes)
        assertEquals(domain.foodCategory, entity.category)
        assertEquals(domain.status, entity.status)
    }
}
