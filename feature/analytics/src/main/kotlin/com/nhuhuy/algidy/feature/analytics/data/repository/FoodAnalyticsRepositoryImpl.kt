package com.nhuhuy.algidy.feature.analytics.data.repository

import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.analytics.domain.repository.FoodAnalyticsRepository
import kotlinx.coroutines.flow.Flow

class FoodAnalyticsRepositoryImpl(
    private val foodRepository: FoodRepository,
) : FoodAnalyticsRepository {

    override fun observeAllFoodItems(): Flow<List<FoodItem>> {
        return foodRepository.observeAllFoodItems()
    }
}
