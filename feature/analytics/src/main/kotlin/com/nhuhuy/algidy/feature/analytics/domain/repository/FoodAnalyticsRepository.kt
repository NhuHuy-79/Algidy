package com.nhuhuy.algidy.feature.analytics.domain.repository

import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.Flow

interface FoodAnalyticsRepository {
    fun observeAllFoodItems(): Flow<List<FoodItem>>
}
