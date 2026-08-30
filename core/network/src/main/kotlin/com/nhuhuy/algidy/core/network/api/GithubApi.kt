package com.nhuhuy.algidy.core.network.api

import com.nhuhuy.algidy.core.network.model.GithubApiResponse
import retrofit2.http.GET

interface GithubApi {
    @GET("releases/latest")
    suspend fun fetchTagName(): GithubApiResponse

    companion object {
        const val GITHUB_RELEASE = "https://api.github.com/repos/NhuHuy-79/Algidy/"
    }
}