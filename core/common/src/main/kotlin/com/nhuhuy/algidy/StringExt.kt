package com.nhuhuy.algidy

fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}