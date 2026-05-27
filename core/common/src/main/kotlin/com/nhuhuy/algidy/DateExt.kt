package com.nhuhuy.algidy

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Duration
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
        .atZone(ZoneId.systemDefault())
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

fun createTodayAt(hour: Int, minute: Int): LocalDateTime {
    return LocalDateTime.now()
        .withHour(hour)
        .withMinute(minute)
        .withSecond(0)
        .withNano(0)
}

fun calculateDelayMillis(
    hour: Int,
    minute: Int
): Long {

    val now = LocalDateTime.now()

    var nextRun = now
        .withHour(hour)
        .withMinute(minute)
        .withSecond(0)
        .withNano(0)


    if (nextRun.isBefore(now) || nextRun.isEqual(now)) {
        nextRun = nextRun.plusDays(1)
    }

    return Duration
        .between(now, nextRun)
        .toMillis()
}

fun LocalDateTime.toMillis(): Long {
    return this
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}