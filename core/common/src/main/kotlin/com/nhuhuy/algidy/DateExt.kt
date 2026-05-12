package com.nhuhuy.algidy

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Int.daysFromNow(): Long {
    return System.currentTimeMillis() + (this.toLong() * 24 * 60 * 60 * 1000)
}

fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

fun Long.formatMillisToDate(): String {
    val date = java.time.Instant.ofEpochMilli(this)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
    val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd")
    return date.format(formatter)
}
