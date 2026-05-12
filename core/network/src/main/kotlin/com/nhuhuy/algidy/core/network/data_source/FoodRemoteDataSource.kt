package com.nhuhuy.algidy.core.network.data_source

import com.nhuhuy.algidy.core.network.api.FoodDetailService
import com.nhuhuy.algidy.core.network.model.FoodApiResponse

interface FoodRemoteDataSource {
    //GET
    suspend fun fetchFoodApiResponse(barcodeString: String) : FoodApiResponse
}

class FoodRemoteDataSourceImpl(
    private val foodDetailService: FoodDetailService
) : FoodRemoteDataSource {
    override suspend fun fetchFoodApiResponse(barcodeString: String): FoodApiResponse {
        return foodDetailService.fetchFoodDetail(barcodeString)
    }
}
