package com.nhuhuy.algidy.core.data

import androidx.annotation.Keep
import kotlinx.coroutines.flow.StateFlow

interface AppCapabilityManager {

    fun isNotificationGranted(): Boolean
    fun isDynamicColorSupported(): Boolean
    fun isBiometricSupported(): Boolean
    val capabilities: StateFlow<AppCapabilities>
}

@Keep
data class AppCapabilities(
    val dynamicColorSupported: Boolean = false,
    val notificationGranted: Boolean = false
)