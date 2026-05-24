package com.nhuhuy.algidy.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.nhuhuy.algidy.core.data.AppCapabilities
import com.nhuhuy.algidy.core.data.AppCapabilityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultAppCapabilityManager(
    private val context: Context,
) : AppCapabilityManager {

    private val _capabilities = MutableStateFlow(AppCapabilities())

    override val capabilities: StateFlow<AppCapabilities> =
        _capabilities
            .asStateFlow()


    override fun isDynamicColorSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    override fun isNotificationGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context)
                .areNotificationsEnabled()
        }
    }
}