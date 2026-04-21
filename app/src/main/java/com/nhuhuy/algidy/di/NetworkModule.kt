package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.network.api.FoodDetailService
import com.nhuhuy.algidy.core.network.api.OPEN_FOOD_FACT_URL
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSource
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSourceImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single<FoodRemoteDataSource> { FoodRemoteDataSourceImpl(get()) }
    single<FoodDetailService> {
        get<Retrofit>().create(FoodDetailService::class.java)
    }
    single {
        val json: Json = get()
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(OPEN_FOOD_FACT_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    single {
        Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }
}
