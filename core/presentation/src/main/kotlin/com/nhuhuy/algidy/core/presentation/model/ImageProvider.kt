package com.nhuhuy.algidy.core.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
sealed interface ImageProvider {
    data class Uri(val path: String? = null) : ImageProvider
    data class Drawable(
        @field:DrawableRes val id: Int
    ) : ImageProvider
}

fun ImageProvider.toUriOrNull(): String? {
    return when (this) {
        is ImageProvider.Uri -> path
        else -> null
    }
}

fun ImageProvider.toDrawableOrNull(): Int? {
    return when (this) {
        is ImageProvider.Drawable -> id
        else -> null
    }
}

fun ImageProvider.toData(): Any? {
    return when (this) {
        is ImageProvider.Uri -> path
        is ImageProvider.Drawable -> id
    }
}