package com.nhuhuy.algidy.core.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.nhuhuy.algidy.core.model.VersionFeatures
import kotlinx.serialization.json.Json
import timber.log.Timber

class AppNewFeaturesReader(
    private val context: Context
) {
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val currentVersionCode: Long get() = context.getAppVersionCode()

    val currentVersionName: String get() = context.getAppVersionName()

    fun getWhatsNewContent(): VersionFeatures? {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.whats_new_7)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val allVersions = jsonConfig.decodeFromString<List<VersionFeatures>>(jsonString)
            allVersions.firstOrNull { it.versionCode == currentVersionCode.toInt() }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    fun Context.getAppVersionName(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                ).versionName
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0).versionName
            } ?: "1.0.0"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "1.0.0"
        }
    }

    fun Context.getAppVersionCode(): Long {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                ).longVersionCode
            } else {
                val packageInfo =
                    @Suppress("DEPRECATION") packageManager.getPackageInfo(packageName, 0)
                packageInfo.longVersionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            1L
        }
    }
}