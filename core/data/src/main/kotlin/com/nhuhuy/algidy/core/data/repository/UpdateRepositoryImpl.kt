package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCall
import com.nhuhuy.algidy.core.domain.repository.UpdateRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.network.api.GithubApi

class UpdateRepositoryImpl(
    private val githubApi: GithubApi,
    private val appDispatchers: AppDispatchers
) : UpdateRepository {
    override suspend fun getTagName(): Resource<String?> {
        return safeCall(appDispatchers.io) {
            githubApi.fetchTagName().tagName
        }
    }
}