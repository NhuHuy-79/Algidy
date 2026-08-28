package com.nhuhuy.algidy.widget.utils

fun String.truncateForWidget(
    maxLength: Int
): String {
    if (length <= maxLength) return this

    return take(maxLength - 1) + "…"
}

const val MAX_LENGTH_SMALL = 18
const val MAX_LENGTH_MEDIUM = 24
const val MAX_LENGTH_LARGE = 32