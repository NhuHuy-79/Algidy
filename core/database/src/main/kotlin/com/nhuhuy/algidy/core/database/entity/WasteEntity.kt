package com.nhuhuy.algidy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nhuhuy.algidy.core.model.food.WasteReason

@Entity(tableName = "waste_records")
data class WasteEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "food_name")
    val foodName: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "unit")
    val unit: String,

    @ColumnInfo(name = "reason")
    val reason: WasteReason,

    @ColumnInfo(name = "date")
    val date: Long
)
