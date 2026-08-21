package com.nhuhuy.algidy

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.toReadableText(): String {
    val date = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy")
    return date.format(formatter)
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

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}