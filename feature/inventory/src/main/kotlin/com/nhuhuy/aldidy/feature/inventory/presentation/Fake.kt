package com.nhuhuy.aldidy.feature.inventory.presentation

import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.ItemUnit
import com.nhuhuy.algidy.core.model.StorageLocation

// Tạo một Object để quản lý dữ liệu mẫu dễ dàng
object SampleData {
    val foodList = listOf(
        FoodItem(
            id = "1",
            name = "Bananas",
            quantity = 2.4,
            itemUnit = ItemUnit.KG,
            location = StorageLocation.PANTRY,
            categoryId = "fruits"
        ),
        FoodItem(
            id = "2",
            name = "Fresh Milk",
            quantity = 1.0,
            itemUnit = ItemUnit.BOTTLE,
            location = StorageLocation.FRIDGE,
            categoryId = "dairy"
        ),
        FoodItem(
            id = "3",
            name = "Wagyu Beef Steaks", // Tên dài để test text wrap
            quantity = 0.5,
            itemUnit = ItemUnit.KG,
            location = StorageLocation.FREEZER,
            categoryId = "meat"
        ),
        FoodItem(
            id = "4",
            name = "Eggs",
            quantity = 12.0,
            itemUnit = ItemUnit.PIECE,
            location = StorageLocation.FRIDGE,
            categoryId = "dairy"
        ),
        FoodItem(
            id = "5",
            name = "Greek Yogurt",
            quantity = 4.0,
            itemUnit = ItemUnit.OTHER,
            location = StorageLocation.FRIDGE,
            categoryId = "dairy"
        ),
        FoodItem(
            id = "6",
            name = "Salmon Fillet",
            quantity = 0.3,
            itemUnit = ItemUnit.KG,
            location = StorageLocation.FRIDGE,
            categoryId = "seafood"
        ),
        FoodItem(
            id = "7",
            name = "Coke Zero",
            quantity = 6.0,
            itemUnit = ItemUnit.BOTTLE,
            location = StorageLocation.FRIDGE,
            categoryId = "drinks"
        )
    )
}