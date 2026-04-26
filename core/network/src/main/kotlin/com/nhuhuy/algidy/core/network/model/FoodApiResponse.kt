package com.nhuhuy.algidy.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodApiResponse(
    @SerialName("code") val code: String?,
    @SerialName("status") val status: Int = 0,
    @SerialName("status_verbose") val statusVerbose: String? = null,
    @SerialName("product") val product: ProductDetail? = null
)

@Serializable
data class ProductDetail(
    @SerialName("product_name") val productName: String? = "",
    @SerialName("brands") val brands: String? = null,
    @SerialName("quantity") val quantity: String? = "",
    @SerialName("image_front_small_url") val imageThumbUrl: String? = null,
    @SerialName("image_front_url") val imageUrl: String? = null,
    @SerialName("categories_tags") val categories: List<String> = emptyList(),
    @SerialName("packaging") val packaging: String? = null
)
