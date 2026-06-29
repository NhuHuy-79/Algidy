package com.nhuhuy.algidy.core.database

import androidx.room.RoomDatabase
import androidx.room.withTransaction


interface TransactionRunner {
    suspend fun run(
        onTransaction: suspend () -> Unit
    )
}

class RoomTransactionRunner(
    private val db: RoomDatabase
) : TransactionRunner {
    override suspend fun run(onTransaction: suspend () -> Unit) {
        db.withTransaction {
            onTransaction()
        }
    }
}

