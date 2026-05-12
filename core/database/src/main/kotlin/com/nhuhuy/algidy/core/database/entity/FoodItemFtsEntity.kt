package com.nhuhuy.algidy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = FoodItemEntity::class)
@Entity(tableName = "inventory_items_fts")
data class InventoryItemFtsEntity(
    @ColumnInfo(name = "name")
    val name: String
)
