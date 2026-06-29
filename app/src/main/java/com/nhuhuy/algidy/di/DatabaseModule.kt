package com.nhuhuy.algidy.di

import androidx.room.Room
import com.nhuhuy.algidy.core.database.AppDatabase
import com.nhuhuy.algidy.core.database.RoomTransactionRunner
import com.nhuhuy.algidy.core.database.TransactionRunner
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "algidy-database"
        )
            .addMigrations(
                AppDatabase.MIGRATION_12_13,
                AppDatabase.MIGRATION_13_14,
                AppDatabase.MIGRATION_14_15
            )
            .build()
    } bind AppDatabase::class
    single { get<AppDatabase>().foodDao() }
    single { get<AppDatabase>().searchDao() }
    single { get<AppDatabase>().categoryDao() }
    single<TransactionRunner> { RoomTransactionRunner(get<AppDatabase>()) }
}
