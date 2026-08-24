package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.ktx.harmonize

data class FoodStateColors(
    // --- FRESHNESS STATES ---
    val fresh: Color,
    val onFresh: Color,
    val freshContainer: Color,
    val onFreshContainer: Color,

    val notice: Color,
    val onNotice: Color,
    val noticeContainer: Color,
    val onNoticeContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val expired: Color,
    val onExpired: Color,
    val expiredContainer: Color,
    val onExpiredContainer: Color,

    // --- HISTORY/ACTION STATES ---
    val wasted: Color,
    val onWasted: Color,
    val wastedContainer: Color,
    val onWastedContainer: Color,

    val consumed: Color,
    val onConsumed: Color,
    val consumedContainer: Color,
    val onConsumedContainer: Color
)

// ☀️ BẢNG MÀU LIGHT MODE
val LightFoodStateColors = FoodStateColors(
    fresh = Color(0xFF1A6C32),
    onFresh = Color(0xFFFFFFFF),
    freshContainer = Color(0xFFA3F5B6),
    onFreshContainer = Color(0xFF00210B),
    notice = Color(0xFF725C00),
    onNotice = Color(0xFFFFFFFF),
    noticeContainer = Color(0xFFFFE07D),
    onNoticeContainer = Color(0xFF231B00),
    warning = Color(0xFF8F4C00),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFDCC0),
    onWarningContainer = Color(0xFF2E1500),
    expired = Color(0xFF7D5250),
    onExpired = Color(0xFFFFFFFF),
    expiredContainer = Color(0xFFFFDAD6),
    onExpiredContainer = Color(0xFF331111),

    wasted = Color(0xFF875230),
    onWasted = Color(0xFFFFFFFF),
    wastedContainer = Color(0xFFFFDBC8),
    onWastedContainer = Color(0xFF321300),
    consumed = Color(0xFF496748),
    onConsumed = Color(0xFFFFFFFF),
    consumedContainer = Color(0xFFCFEBCB),
    onConsumedContainer = Color(0xFF06210A)
)

// 🌙 BẢNG MÀU DARK MODE
val DarkFoodStateColors = FoodStateColors(
    fresh = Color(0xFF88D99C),
    onFresh = Color(0xFF003917),
    freshContainer = Color(0xFF005224),
    onFreshContainer = Color(0xFFA3F5B6),
    notice = Color(0xFFE6C449),
    onNotice = Color(0xFF3C2F00),
    noticeContainer = Color(0xFF564500),
    onNoticeContainer = Color(0xFFFFE07D),
    warning = Color(0xFFFFB778),
    onWarning = Color(0xFF4C2700),
    warningContainer = Color(0xFF6D3900),
    onWarningContainer = Color(0xFFFFDCC0),
    expired = Color(0xFFEBB9B6),
    onExpired = Color(0xFF4A2524),
    expiredContainer = Color(0xFF633B39),
    onExpiredContainer = Color(0xFFFFDAD6),

    wasted = Color(0xFFC28B63),
    onWasted = Color(0xFF2A170B),
    wastedContainer = Color(0xFF4A2F1D),
    onWastedContainer = Color(0xFFFFDBC8),
    consumed = Color(0xFF7FAE7D),
    onConsumed = Color(0xFF0F1F10),
    consumedContainer = Color(0xFF233425),
    onConsumedContainer = Color(0xFFCFEBCB)
)

// Provider
val LocalFoodStateColors = staticCompositionLocalOf { LightFoodStateColors }

fun FoodStateColors.harmonize(colorScheme: ColorScheme): FoodStateColors {
    return this.copy(
        fresh = fresh.harmonize(colorScheme.primary),
        notice = notice.harmonize(colorScheme.primary),
        warning = warning.harmonize(colorScheme.primary),
        expired = expired.harmonize(colorScheme.primary),
        wasted = wasted.harmonize(colorScheme.primary),
        consumed = consumed.harmonize(colorScheme.primary),
        freshContainer = freshContainer.harmonize(colorScheme.primary),
        noticeContainer = noticeContainer.harmonize(colorScheme.primary),
        warningContainer = warningContainer.harmonize(colorScheme.primary),
        expiredContainer = expiredContainer.harmonize(colorScheme.primary),
        onFresh = onFresh.harmonize(colorScheme.onPrimary),
        onNotice = onNotice.harmonize(colorScheme.onPrimary),
        onWarning = onWarning.harmonize(colorScheme.onPrimary),
        onExpired = onExpired.harmonize(colorScheme.onPrimary),
        onFreshContainer = onFreshContainer.harmonize(colorScheme.onPrimaryContainer),
        onNoticeContainer = onNoticeContainer.harmonize(colorScheme.onPrimaryContainer),
        onWarningContainer = onWarningContainer.harmonize(colorScheme.onPrimaryContainer),
        onExpiredContainer = onExpiredContainer.harmonize(colorScheme.onPrimaryContainer),
    )
}