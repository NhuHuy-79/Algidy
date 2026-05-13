package com.nhuhuy.algidy

import android.app.Application
import com.nhuhuy.algidy.di.dataModule
import com.nhuhuy.algidy.di.databaseModule
import com.nhuhuy.algidy.di.networkModule
import com.nhuhuy.algidy.feature.analytics.di.analyticsModule
import com.nhuhuy.algidy.feature.detail.di.detailModule
import com.nhuhuy.algidy.feature.inventory.di.inventoryModule
import com.nhuhuy.algidy.feature.scanner.di.scannerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber


class AlgidyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
        startKoin {
            androidContext(this@AlgidyApp)
            androidLogger(level = Level.DEBUG)
            modules(
                listOf(
                    databaseModule,
                    dataModule,
                    networkModule,
                    inventoryModule,
                    detailModule,
                    analyticsModule,
                    scannerModule
                )
            )
        }
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }
}

