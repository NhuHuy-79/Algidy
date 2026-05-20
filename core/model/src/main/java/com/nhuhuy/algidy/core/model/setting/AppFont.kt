package com.nhuhuy.algidy.core.model.setting

import com.nhuhuy.algidy.core.model.setting.AppFont.Companion.fromStoreKey

enum class AppFont(val fontName: String, val storeKey: String) {
    DEFAULT("Roboto", "roboto"),
    INTER("Inter", "inter"),
    POPPINS("Poppins", "poppins"),
    JAKARTA("Plus Jakarta Sans", "jakarta"),
    MONTSERRAT("Montserrat", "montserrat"),
    OPEN_SANS("Open Sans", "open_sans"),
    NUNITO("Nunito", "nunito");

    companion object {
        fun fromStoreKey(key: String?): AppFont {
            return entries.find { it.storeKey == key } ?: DEFAULT
        }
    }
}

fun String?.toAppFont(): AppFont {
    return fromStoreKey(this)
}