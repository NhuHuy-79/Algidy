package com.nhuhuy.algidy.widget.utils

import androidx.compose.ui.unit.sp
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle

object WidgetTypography {
    val title = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
    val body = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    val bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
    val label = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
    val labelSmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    )
    val value = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}