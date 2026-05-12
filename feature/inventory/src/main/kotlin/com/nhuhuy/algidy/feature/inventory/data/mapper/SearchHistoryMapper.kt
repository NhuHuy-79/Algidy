package com.nhuhuy.algidy.feature.inventory.data.mapper

import com.nhuhuy.algidy.core.database.entity.SearchHistoryEntity
import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult

fun SearchHistoryEntity.toDomain() = HistoryResult(
    id = id,
    name = query
)

fun HistoryResult.toSearchHistoryEntity() = SearchHistoryEntity(
    query = name
)