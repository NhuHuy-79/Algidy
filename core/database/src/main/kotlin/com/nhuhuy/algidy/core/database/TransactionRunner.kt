package com.nhuhuy.algidy.core.database

import androidx.room.withTransaction

interface TransactionRunner {
    suspend fun <T> run(
        block: suspend () -> T
    ): T
}

class TransactionRunnerImpl(
    private val database: AppDatabase
) : TransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T {
        return database.withTransaction(block)
    }
}
