package com.nhuhuy.algidy.core.network.data_source

import com.nhuhuy.algidy.core.network.api.FoodApi
import com.nhuhuy.algidy.core.network.model.FoodApiResponse

interface FoodRemoteDataSource {
    //GET
    suspend fun fetchFoodApiResponse(barcodeString: String) : FoodApiResponse
}

class FoodRemoteDataSourceImpl(
    private val foodApi: FoodApi
) : FoodRemoteDataSource {
    override suspend fun fetchFoodApiResponse(barcodeString: String): FoodApiResponse {
        return foodApi.fetchFoodDetail(barcodeString)
    }
}
