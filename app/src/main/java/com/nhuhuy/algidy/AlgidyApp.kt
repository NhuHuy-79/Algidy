package com.nhuhuy.algidy

import android.app.Application
import com.nhuhuy.algidy.di.dataModule
import com.nhuhuy.algidy.di.viewModelModule
import databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level


class AlgidyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlgidyApp)
            androidLogger(level = Level.DEBUG)
            modules(
                listOf(
                    databaseModule,
                    dataModule,
                    viewModelModule
                )
            )
        }
    }
}