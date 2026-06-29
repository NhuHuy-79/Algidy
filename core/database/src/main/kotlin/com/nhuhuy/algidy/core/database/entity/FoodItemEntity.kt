package com.nhuhuy.algidy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.StorageLocation

/**
 * Entity representing a food item in the inventory.
 * Linked to CategoryEntity via category_id with Cascade Delete.
 */
@Entity(
    tableName = "food_items",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["category_id"])]
)
data class FoodItemEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "normalized_name", defaultValue = "")
    val normalizedName: String = "",

    @ColumnInfo(name = "category_id")
    val categoryId: String? = null,

    @ColumnInfo(name = "location")
    val location: StorageLocation,

    @ColumnInfo(name = "purchase_date")
    val purchaseDate: Long,

    @ColumnInfo(name = "expiry_date")
    val expiryDate: Long,

    @ColumnInfo(name = "image_uri")
    val imageUri: String?,

    @ColumnInfo(name = "notes")
    val notes: String,

    @ColumnInfo(name = "status")
    val status: FoodStatus = FoodStatus.ACTIVE,

    @ColumnInfo(name = "resolved_date")
    val resolvedDate: Long? = null
)
