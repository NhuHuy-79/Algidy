package com.nhuhuy.algidy.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree(
    private val crashlytics: FirebaseCrashlytics
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        crashlytics.log("$tag: $message")

        if (priority == android.util.Log.ERROR || priority == android.util.Log.WARN) {
            if (t != null) {
                crashlytics.recordException(t)
            } else {
                crashlytics.recordException(Exception(message))
            }
        }
    }
}