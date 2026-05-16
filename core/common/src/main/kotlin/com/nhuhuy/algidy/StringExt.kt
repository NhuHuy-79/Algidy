package com.nhuhuy.algidy

import android.icu.text.Transliterator

fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

fun String.toGenericNormalized(): String {
    val id = "Any-Latin; Latin-ASCII; Lower()"
    val transliterator = Transliterator.getInstance(id)

    return transliterator.transliterate(this)
}