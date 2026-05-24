package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.data.AppCapabilityManager
import com.nhuhuy.algidy.core.datastore.DeviceCapableDataStore
import com.nhuhuy.algidy.feature.settings.domain.model.Capability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CheckCapabilityUseCase(
    private val deviceCapableDataStore: DeviceCapableDataStore,
    private val appCapabilityManager: AppCapabilityManager
) {
    suspend fun init() {
        deviceCapableDataStore.setDynamicColorSupported(appCapabilityManager.isDynamicColorSupported())
        deviceCapableDataStore.setNotificationGranted(appCapabilityManager.isNotificationGranted())
    }

    suspend fun updateBiometric(value: Boolean) {
        deviceCapableDataStore.setBiometricSupported(value)
    }

    fun observe(): Flow<Capability> {
        return combine(
            deviceCapableDataStore.dynamicColorSupported,
            deviceCapableDataStore.notificationGranted,
            deviceCapableDataStore.biometricSupported
        ) { dynamicColorSupported, notificationGranted, biometricSupported ->
            Capability(
                dynamicColorSupported = dynamicColorSupported,
                notificationGranted = notificationGranted,
                biometricSupported = biometricSupported
            )
        }
    }
}