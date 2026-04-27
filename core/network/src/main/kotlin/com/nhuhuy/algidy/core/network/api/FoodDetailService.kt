package com.nhuhuy.algidy.core.network.api

import com.nhuhuy.algidy.core.network.model.FoodApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodDetailService {

    // OFF dùng cấu trúc: api/v2/product/{barcode}.json
    @GET("api/v2/product/{barcode}.json")
    suspend fun fetchFoodDetail(
        @Path("barcode") barcodeString: String,
        @Query("fields") fields: String = "product_name,brands,image_front_url,categories,categories_tags"
    ) : FoodApiResponse

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
    }
}