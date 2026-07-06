package com.nhuhuy.algidy.utils

import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.datastore.migration.AppearanceMigrator
import com.nhuhuy.algidy.core.datastore.migration.BaseMigrator
import com.nhuhuy.algidy.core.datastore.migration.GeneralMigrator
import com.nhuhuy.algidy.core.datastore.migration.NotificationMigrator
import kotlinx.coroutines.withContext
import timber.log.Timber

class AppInitializer(
    private val appDispatchers: AppDispatchers,
    private val notificationMigrator: NotificationMigrator,
    private val appearanceMigrator: AppearanceMigrator,
    private val generalMigrator: GeneralMigrator
) {
    suspend fun initialize() {
        withContext(appDispatchers.io) {
            runMigrator(notificationMigrator)
            runMigrator(appearanceMigrator)
            runMigrator(generalMigrator)
        }
    }

    private suspend fun <T> runMigrator(
        migrator: BaseMigrator<T>
    ) {
        try {
            if (!migrator.shouldMigrate()) {
                Timber.d("${migrator::class.simpleName} needn't migrate")
                return
            }
            Timber.d("Migrating ${migrator::class.simpleName}...")
            migrator.migrate()
            migrator.cleanUp()
            Timber.d("${migrator::class.simpleName} completed")
        } catch (e: Exception) {
            Timber.e(
                e,
                "${migrator::class.simpleName} failed"
            )
        }
    }
}