package com.nhuhuy.aldidy.feature.inventory.presentation

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation

// Tạo một Object để quản lý dữ liệu mẫu dễ dàng
object SampleData {
    val now = System.currentTimeMillis()
    const val dayInMs = 24 * 60 * 60 * 1000L
    val foodList = listOf(
        FoodItem(
            id = "1",
            name = "Bananas",
            quantity = 2.4,
            itemUnit = ItemUnit.KG,
            location = StorageLocation.PANTRY,
            categoryId = "fruits",
            purchaseDate = now - (2 * dayInMs),
            expiryDate = now + (7 * dayInMs)
        ),
        FoodItem(
            id = "2",
            name = "Fresh Milk",
            quantity = 1.0,
            itemUnit = ItemUnit.BOTTLE,
            location = StorageLocation.FRIDGE,
            categoryId = "dairy",
            purchaseDate = now - (5 * dayInMs),
            expiryDate = now + (1 * dayInMs)
        ),
        // 3. Thịt bò: Mua 10 ngày trước, còn tận 20 ngày (đồ đông lạnh) -> TƯƠI (Xanh)
        FoodItem(
            id = "3",
            name = "Wagyu Beef Steaks",
            quantity = 0.5,
            itemUnit = ItemUnit.KG,
            location = StorageLocation.FREEZER,
            categoryId = "meat",
            purchaseDate = now - (10 * dayInMs),
            expiryDate = now + (20 * dayInMs)
        ),
        // 4. Trứng: Mua 10 ngày trước, còn 5 ngày nữa -> CẢNH BÁO (Vàng)
        FoodItem(
            id = "4",
            name = "Eggs",
            quantity = 12.0,
            itemUnit = ItemUnit.PIECE,
            location = StorageLocation.FRIDGE,
            categoryId = "dairy",
            purchaseDate = now - (10 * dayInMs),
            expiryDate = now + (4 * dayInMs)
        ),
        // 5. Sữa chua: Mua 10 ngày trước, đã hết hạn 2 ngày trước -> HẾT HẠN (Đỏ)
        FoodItem(
            id = "5",
            name = "Greek Yogurt",
            quantity = 4.0,
            itemUnit = ItemUnit.BOTTLE,
            location = StorageLocation.FRIDGE,
            categoryId = "dairy",
            purchaseDate = now - (10 * dayInMs),
            expiryDate = now - (2 * dayInMs)
        ),
        // 6. Cá hồi: Mua hôm qua, còn 2 ngày nữa hỏng -> KHẨN CẤP (Cam)
        FoodItem(
            id = "6",
            name = "Salmon Fillet",
            quantity = 0.3,
            itemUnit = ItemUnit.KG,
            location = StorageLocation.FRIDGE,
            categoryId = "seafood",
            purchaseDate = now - (1 * dayInMs),
            expiryDate = now + (2 * dayInMs)
        ),
        // 7. Coke: Hạn dùng cực dài (6 tháng nữa) -> TƯƠI (Xanh)
        FoodItem(
            id = "7",
            name = "Coke Zero",
            quantity = 6.0,
            itemUnit = ItemUnit.BOTTLE,
            location = StorageLocation.FRIDGE,
            categoryId = "drinks",
            purchaseDate = now - (30 * dayInMs),
            expiryDate = now + (180 * dayInMs)
        )
    )
}