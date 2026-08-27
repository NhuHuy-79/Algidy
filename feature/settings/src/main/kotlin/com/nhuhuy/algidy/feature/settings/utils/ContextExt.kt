package com.nhuhuy.algidy.feature.settings.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openUrl(url: String): Boolean {
    return runCatching {
        startActivity(
            Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }.isSuccess
}

fun Context.sendEmail(
    email: String,
    subject: String? = null
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$email".toUri()
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    startActivity(intent)
}