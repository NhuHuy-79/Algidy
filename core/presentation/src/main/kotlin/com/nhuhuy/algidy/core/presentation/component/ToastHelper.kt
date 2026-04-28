package com.nhuhuy.algidy.core.presentation.component

import android.content.Context
import android.widget.Toast

fun Context.showShortToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    return Toast.makeText(this, message, duration).show()
}