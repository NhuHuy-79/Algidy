package com.nhuhuy.algidy.widget.utils

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.SizeMode

object WidgetLayoutConfig {
    val SMALL = DpSize(180.dp, 110.dp)
    val MEDIUM = DpSize(250.dp, 110.dp)
    val LARGE = DpSize(320.dp, 110.dp)

    val responsiveSizes = setOf(SMALL, MEDIUM, LARGE)

    val defaultSizeMode = SizeMode.Responsive(responsiveSizes)

    enum class WidgetMode { COMPACT, MEDIUM, EXPANDED }

    fun getModeForSize(size: DpSize): WidgetMode =
        when {
            size.width >= 320.dp -> WidgetMode.EXPANDED
            size.width >= 250.dp -> WidgetMode.MEDIUM
            else -> WidgetMode.COMPACT
        }
}