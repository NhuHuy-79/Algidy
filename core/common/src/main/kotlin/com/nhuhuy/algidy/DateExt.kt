package com.nhuhuy.algidy

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
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

fun getStartOfWeekMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    val now = LocalDateTime.now(zoneId)

    val startOfWeek = now.with(DayOfWeek.MONDAY)
        .truncatedTo(ChronoUnit.DAYS)

    return startOfWeek.atZone(zoneId).toInstant().toEpochMilli()
}

fun getEndOfWeekMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    val now = LocalDateTime.now(zoneId)

    // Tìm ngày Chủ Nhật tuần này lúc 23:59:59
    val endOfWeek = now.with(DayOfWeek.SUNDAY)
        .withHour(23)
        .withMinute(59)
        .withSecond(59)
        .withNano(999_999_999)

    return endOfWeek.atZone(zoneId).toInstant().toEpochMilli()
}