package com.nhuhuy.algidy

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.nhuhuy.algidy.core.data.FOLDER_IMAGE
import com.nhuhuy.algidy.core.notifications.data.NotificationChannelManager
import com.nhuhuy.algidy.core.notifications.di.notificationModule
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler
import com.nhuhuy.algidy.di.appModule
import com.nhuhuy.algidy.di.dataModule
import com.nhuhuy.algidy.di.databaseModule
import com.nhuhuy.algidy.di.networkModule
import com.nhuhuy.algidy.feature.analytics.di.analyticsModule
import com.nhuhuy.algidy.feature.detail.di.detailModule
import com.nhuhuy.algidy.feature.food_entry.di.foodEntryModule
import com.nhuhuy.algidy.feature.inventory.di.inventoryModule
import com.nhuhuy.algidy.feature.scanner.di.scannerModule
import com.nhuhuy.algidy.feature.settings.di.settingModule
import com.nhuhuy.algidy.utils.CrashlyticsTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber


class AlgidyApp : Application(), KoinComponent, SingletonImageLoader.Factory {
    private val workerScheduler: WorkerScheduler by inject()
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.filesDir.resolve(FOLDER_IMAGE))
                    .maxSizeBytes(100L * 1024 * 1024) // 100MB
                    .build()
            }
            .components {
                add(OkHttpNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        // Timber & Crashlytics setup
        plantTimber()

        //Koin for DI
        startKoin {
            androidContext(this@AlgidyApp)
            androidLogger(level = Level.DEBUG)
            workManagerFactory()
            modules(
                listOf(
                    appModule,
                    databaseModule,
                    dataModule,
                    networkModule,
                    inventoryModule,
                    detailModule,
                    analyticsModule,
                    scannerModule,
                    foodEntryModule,
                    notificationModule,
                    settingModule
                )
            )
        }

        //Notification
        NotificationChannelManager.createAllChannels(this)


        //Worker Scheduler
        workerScheduler.apply {
            scheduleCheckExpiryWorker()
            scheduleWeeklyReportWorker()
            scheduleWeeklyCleanUpFileWorker()
        }

    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            firebaseCrashlytics = Firebase.crashlytics
            firebaseCrashlytics.isCrashlyticsCollectionEnabled = true
            Timber.plant(CrashlyticsTree(firebaseCrashlytics))
        }
    }
}
