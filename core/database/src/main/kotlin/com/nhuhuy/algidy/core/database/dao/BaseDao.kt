package com.nhuhuy.algidy.core.database.dao

import androidx.room.Upsert

interface BaseDao<T> {
    @Upsert
    suspend fun upsertAll(list: List<T>)

    @Upsert
    suspend fun update(entity: T)
}