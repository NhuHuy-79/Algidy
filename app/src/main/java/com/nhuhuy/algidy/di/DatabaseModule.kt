package com.nhuhuy.algidy.di

import androidx.room.Room
import com.nhuhuy.algidy.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "algidy-database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AppDatabase>().foodDao() }
    single { get<AppDatabase>().wasteDao() }
}