package com.nhuhuy.algidy.core.datastore.migration

interface BaseMigrator {
    suspend fun shouldMigrate(): Boolean
    suspend fun migrate()
    suspend fun cleanUp()
}