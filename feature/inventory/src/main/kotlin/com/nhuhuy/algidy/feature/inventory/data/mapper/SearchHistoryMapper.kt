package com.nhuhuy.algidy.feature.inventory.data.mapper

import com.nhuhuy.algidy.core.database.entity.SearchHistoryEntity
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory

fun SearchHistoryEntity.toDomain() = SearchHistory(
    id = id,
    name = query,
    timeStamp = timestamp
)

fun SearchHistory.toSearchHistoryEntity() = SearchHistoryEntity(
    query = name
)