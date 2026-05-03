package com.nhuhuy.algidy

import android.app.Application
import com.nhuhuy.algidy.di.dataModule
import com.nhuhuy.algidy.di.databaseModule
import com.nhuhuy.algidy.di.networkModule
import com.nhuhuy.algidy.di.useCaseModule
import com.nhuhuy.algidy.di.viewModelModule
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
                    viewModelModule,
                    networkModule,
                    useCaseModule,
                )
            )
        }
    }

    private fun plantTimber(){
        Timber.plant(Timber.DebugTree())
    }
}

