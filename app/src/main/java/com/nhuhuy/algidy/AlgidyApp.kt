package com.nhuhuy.algidy

import android.app.Application
import com.nhuhuy.algidy.di.dataModule
import databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AlgidyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlgidyApp)
            modules(
                listOf(
                    databaseModule,
                    dataModule
                )
            )
        }
    }
}