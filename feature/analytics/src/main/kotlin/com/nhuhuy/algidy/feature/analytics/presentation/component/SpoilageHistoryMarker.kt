package com.nhuhuy.algidy.feature.analytics.presentation.component

import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget

class SpoilageHistoryMarker(
    private val onValueChange: (Pair<Int, Int>) -> Unit,
    private val onValueHide: () -> Unit,
) : CartesianMarkerVisibilityListener {
    override fun onShown(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
        val target = targets.firstOrNull() as? LineCartesianLayerMarkerTarget ?: return
        val firstValue = target.points.getOrNull(0)?.entry?.y
        val secondValue = target.points.getOrNull(1)?.entry?.y
        if (firstValue != null && secondValue != null) {
            onValueChange(firstValue.toInt() to secondValue.toInt())
        }
    }

    override fun onHidden(marker: CartesianMarker) {
        onValueHide()
    }

    override fun onUpdated(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {
        val target = targets.firstOrNull() as? LineCartesianLayerMarkerTarget ?: return
        val firstValue = target.points.getOrNull(0)?.entry?.y
        val secondValue = target.points.getOrNull(1)?.entry?.y
        if (firstValue != null && secondValue != null) {
            onValueChange(firstValue.toInt() to secondValue.toInt())
        }
    }
}