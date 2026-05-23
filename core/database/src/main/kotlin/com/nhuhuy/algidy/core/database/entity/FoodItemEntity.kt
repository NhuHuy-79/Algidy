package com.nhuhuy.algidy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "normalized_name", defaultValue = "")
    val normalizedName: String = "",

    @ColumnInfo(name = "category_id")
    val categoryId: String,

    @ColumnInfo(name = "location")
    val location: StorageLocation,

    @ColumnInfo(name = "quantity")
    val quantity: Double,

    @ColumnInfo(name = "item_unit")
    val itemUnit: ItemUnit,

    @ColumnInfo(name = "purchase_date")
    val purchaseDate: Long,

    @ColumnInfo(name = "expiry_date")
    val expiryDate: Long,

    @ColumnInfo(name = "image_uri")
    val imageUri: String?,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,

    @ColumnInfo(name = "notes")
    val notes: String,

    @ColumnInfo(name = "food_category")
    val category: DefaultFoodCategory,

    @ColumnInfo(name = "status")
    val status: FoodStatus = FoodStatus.ACTIVE,

    @ColumnInfo(name = "resolved_date")
    val resolvedDate: Long? = null
)
