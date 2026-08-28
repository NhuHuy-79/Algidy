package com.nhuhuy.algidy.widget.usecase

import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.toLocalDate
import java.time.DayOfWeek
import java.time.LocalDate

class GetFoodsUseCase(
    private val foodRepository: FoodRepository
) {

    suspend operator fun invoke(): List<FoodItem> {
        return foodRepository.getAllFoodItems()
    }

    suspend fun getThisWeek(): List<FoodItem> {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val endOfWeek = today.with(DayOfWeek.SUNDAY)

        return foodRepository.getAllFoodItems()
            .filter { food ->
                food.expiryDate.toLocalDate() in startOfWeek..endOfWeek && food.status == FoodStatus.ACTIVE
            }
            .sortedBy { it.expiryDate }
    }
}