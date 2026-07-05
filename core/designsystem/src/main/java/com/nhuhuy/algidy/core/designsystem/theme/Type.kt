package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.nhuhuy.algidy.core.designsystem.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val MontserratFontFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.montserrat_regular, weight = FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.montserrat_bold, weight = FontWeight.Bold),
    androidx.compose.ui.text.font.Font(R.font.montserrat_semi_bold, weight = FontWeight.SemiBold),
    androidx.compose.ui.text.font.Font(R.font.montserrat_black, weight = FontWeight.Black),
    androidx.compose.ui.text.font.Font(R.font.montserrat_medium, weight = FontWeight.Medium),
)

fun getTypographyForFont(): Typography {
    /* val googleFontName = GoogleFont(fontName)
     val dynamicFontFamily = FontFamily(
         Font(googleFont = googleFontName, fontProvider = provider, weight = FontWeight.Normal),
         Font(googleFont = googleFontName, fontProvider = provider, weight = FontWeight.Medium),
         Font(googleFont = googleFontName, fontProvider = provider, weight = FontWeight.SemiBold),
         Font(googleFont = googleFontName, fontProvider = provider, weight = FontWeight.Bold)
     )*/

    val defaultTypography = Typography()

    return defaultTypography.run {
        copy(
            displayLarge = displayLarge.copy(fontFamily = MontserratFontFamily),
            displayMedium = displayMedium.copy(fontFamily = MontserratFontFamily),
            displaySmall = displaySmall.copy(fontFamily = MontserratFontFamily),
            headlineLarge = headlineLarge.copy(fontFamily = MontserratFontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = MontserratFontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = MontserratFontFamily),
            titleLarge = titleLarge.copy(fontFamily = MontserratFontFamily),
            titleMedium = titleMedium.copy(fontFamily = MontserratFontFamily),
            titleSmall = titleSmall.copy(fontFamily = MontserratFontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = MontserratFontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = MontserratFontFamily),
            bodySmall = bodySmall.copy(fontFamily = MontserratFontFamily),
            labelLarge = labelLarge.copy(fontFamily = MontserratFontFamily),
            labelMedium = labelMedium.copy(fontFamily = MontserratFontFamily),
            labelSmall = labelSmall.copy(fontFamily = MontserratFontFamily)
        )
    }
}
