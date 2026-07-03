package com.nhuhuy.algidy.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
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

    return Typography(
        displayLarge = TextStyle(fontFamily = MontserratFontFamily),
        displayMedium = TextStyle(fontFamily = MontserratFontFamily),
        displaySmall = TextStyle(fontFamily = MontserratFontFamily),
        headlineLarge = TextStyle(fontFamily = MontserratFontFamily),
        headlineMedium = TextStyle(fontFamily = MontserratFontFamily),
        headlineSmall = TextStyle(fontFamily = MontserratFontFamily),
        titleLarge = TextStyle(fontFamily = MontserratFontFamily),
        titleMedium = TextStyle(fontFamily = MontserratFontFamily),
        titleSmall = TextStyle(fontFamily = MontserratFontFamily),
        bodyLarge = TextStyle(fontFamily = MontserratFontFamily),
        bodyMedium = TextStyle(fontFamily = MontserratFontFamily),
        bodySmall = TextStyle(fontFamily = MontserratFontFamily),
        labelLarge = TextStyle(fontFamily = MontserratFontFamily),
        labelMedium = TextStyle(fontFamily = MontserratFontFamily),
        labelSmall = TextStyle(fontFamily = MontserratFontFamily)
    )
}
