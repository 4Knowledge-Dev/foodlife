package com.forknowledge.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.forknowledge.core.ui.R

// Set of Material typography styles to start with
/*val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val openSans = GoogleFont("Open Sans")*/

val openSansFamily = FontFamily(
    Font(R.font.open_sans_regular, FontWeight.Normal),
    Font(R.font.open_sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.open_sans_extra_bold, FontWeight.ExtraBold),
)

val buttonTextStyle = TextStyle(
    fontFamily = openSansFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp
)

val buttonTextDialog = TextStyle(
    fontFamily = openSansFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 18.sp,
    color = Black05172C
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 21.sp
    ),
    displayLarge = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 28.sp
    ),
    displayMedium = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 21.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 13.sp
    ),
    labelLarge = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.W600
    ),
    labelMedium = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.W600
    ),
    labelSmall = TextStyle(
        fontFamily = openSansFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.W600
    ),
    /* Other default text styles to override
titleLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
),
labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
*/
)