package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.core.network.api.FoodApi
import com.nhuhuy.algidy.core.network.api.FoodApi.Companion.BASE_URL
import com.nhuhuy.algidy.core.network.api.GithubApi
import com.nhuhuy.algidy.core.network.api.GithubApi.Companion.GITHUB_RELEASE
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSource
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSourceImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val ALGIDY_API = "ALGIDY_API"
private const val GITHUB_API = "GITHUB_API"

val networkModule = module {
    single<FoodRemoteDataSource> { FoodRemoteDataSourceImpl(get()) }
    single<FoodApi> {
        get<Retrofit>(qualifier = named(ALGIDY_API)).create(FoodApi::class.java)
    }

    single<GithubApi> {
        get<Retrofit>(qualifier = named(GITHUB_API)).create(GithubApi::class.java)
    }

    single(qualifier = named(ALGIDY_API)) {
        val json: Json = get()
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    single(qualifier = named(GITHUB_API)) {
        val json: Json = get()
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(GITHUB_RELEASE)
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
