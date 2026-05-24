package com.nhuhuy.algidy.core.data

import kotlinx.coroutines.flow.StateFlow

interface AppCapabilityManager {

    fun isNotificationGranted(): Boolean
    fun isDynamicColorSupported(): Boolean
    val capabilities: StateFlow<AppCapabilities>
}

data class AppCapabilities(
    val dynamicColorSupported: Boolean = false,
    val notificationGranted: Boolean = false
)