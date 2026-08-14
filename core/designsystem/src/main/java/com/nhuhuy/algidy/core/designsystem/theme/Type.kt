package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.designsystem.R

/**
 * Google Sans Flex
 *
 * The font is a variable font, so we can control:
 *
 * - Weight (wght)
 * - Width (wdth)
 * - Roundness (ROND)
 *
 * These axes allow the typography to feel more expressive
 * without needing multiple font files.
 */

@OptIn(ExperimentalTextApi::class)
private val GoogleSansFlexFontFamily = FontFamily(

    // 400 - Regular
    Font(
        resId = R.font.google_sans_flex_font,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
            FontVariation.width(100f),
            FontVariation.Setting("ROND", 20f),
        ),
    ),

    // 500 - Medium
    Font(
        resId = R.font.google_sans_flex_font,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(500),
            FontVariation.width(102.5f),
            FontVariation.Setting("ROND", 35f),
        ),
    ),

    // 600 - SemiBold
    Font(
        resId = R.font.google_sans_flex_font,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600),
            FontVariation.width(105f),
            FontVariation.Setting("ROND", 50f),
        ),
    ),

    // 700 - Bold
    Font(
        resId = R.font.google_sans_flex_font,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(700),
            FontVariation.width(105f),
            FontVariation.Setting("ROND", 50f),
        ),
    ),
)

fun getTypographyForFont(): Typography {

    val defaultTypography = Typography()

    return defaultTypography.copy(

        // -----------------------------------------------------------------
        // Display
        // -----------------------------------------------------------------

        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        displayMedium = defaultTypography.displayMedium.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        displaySmall = defaultTypography.displaySmall.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        // -----------------------------------------------------------------
        // Headline
        // -----------------------------------------------------------------

        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        headlineSmall = defaultTypography.headlineSmall.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        // -----------------------------------------------------------------
        // Title
        // -----------------------------------------------------------------

        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        titleMedium = defaultTypography.titleMedium.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        titleSmall = defaultTypography.titleSmall.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        // -----------------------------------------------------------------
        // Body
        // -----------------------------------------------------------------

        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        bodyMedium = defaultTypography.bodyMedium.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),

        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = GoogleSansFlexFontFamily,
        ),
    )
}