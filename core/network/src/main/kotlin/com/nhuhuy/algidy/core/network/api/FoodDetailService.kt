package com.nhuhuy.algidy.core.network.api

import com.nhuhuy.algidy.core.network.model.FoodApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val OPEN_FOOD_FACT_URL = "https://world.openfoodfacts.net/api/v2/product/"

interface FoodDetailService {
    @GET(OPEN_FOOD_FACT_URL)
    suspend fun fetchFoodDetail(
        @Query("barcode") barcodeString: String
    ) : FoodApiResponse
}