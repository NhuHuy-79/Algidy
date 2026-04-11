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