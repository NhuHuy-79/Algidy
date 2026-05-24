package com.nhuhuy.algidy.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.nhuhuy.algidy.core.datastore.utils.set
import kotlinx.coroutines.flow.Flow

object DeviceKey {
    val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color_supported")
    val NOTIFICATION_GRANTED = booleanPreferencesKey("notification_granted")
    val BIOMETRIC_SUPPORTED = booleanPreferencesKey("biometric_supported")
}

interface DeviceCapableDataStore {
    val dynamicColorSupported: Flow<Boolean>
    val notificationGranted: Flow<Boolean>
    val biometricSupported: Flow<Boolean>

    suspend fun setDynamicColorSupported(supported: Boolean)
    suspend fun setNotificationGranted(granted: Boolean)
    suspend fun setBiometricSupported(supported: Boolean)
}

class DeviceCapableDataStoreImpl(
    context: Context
) : DeviceCapableDataStore {
    private val deviceDataStore = context.dataStore

    override val dynamicColorSupported: Flow<Boolean>
        get() = deviceDataStore.data.set(
            key = DeviceKey.DYNAMIC_COLOR, defaultValue = true
        )
    override val notificationGranted: Flow<Boolean>
        get() = deviceDataStore.data.set(
            key = DeviceKey.NOTIFICATION_GRANTED, defaultValue = false
        )
    override val biometricSupported: Flow<Boolean>
        get() = deviceDataStore.data.set(
            key = DeviceKey.BIOMETRIC_SUPPORTED, defaultValue = true
        )

    override suspend fun setDynamicColorSupported(supported: Boolean) {
        deviceDataStore.set(key = DeviceKey.DYNAMIC_COLOR, value = supported)
    }

    override suspend fun setNotificationGranted(granted: Boolean) {
        deviceDataStore.set(key = DeviceKey.NOTIFICATION_GRANTED, value = granted)
    }

    override suspend fun setBiometricSupported(supported: Boolean) {
        deviceDataStore.set(key = DeviceKey.BIOMETRIC_SUPPORTED, value = supported)
    }

}