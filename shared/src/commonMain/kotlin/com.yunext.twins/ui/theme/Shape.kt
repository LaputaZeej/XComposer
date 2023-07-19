package com.yunext.twins.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunext.twins.ui.compoents.China

val ItemShape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 12.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

internal fun Modifier.debugBorder() = this.border(1.dp, China.r_luo_xia_hong)


//val fontName = GoogleFont("Lato")
//
//val provider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)
//val fontFamily = FontFamily(
//    Font(googleFont = fontName, fontProvider = provider)
//)
//// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//)
//
//val TitleBarStyle = TextStyle(
//    fontSize = 22.sp,
//    fontWeight = FontWeight(700),
//    letterSpacing = 0.5.sp,
//    fontFamily = fontFamily
//)
//
//val HeadingStyle = TextStyle(
//    fontSize = 24.sp,
//    fontWeight = FontWeight(600),
//    letterSpacing = 0.5.sp,
//    fontFamily = fontFamily
//)
//
//val SmallHeadingStyle = TextStyle(
//    fontSize = 16.sp,
//    fontWeight = FontWeight(600),
//    letterSpacing = 0.5.sp,
//    fontFamily = fontFamily
//)
//
//val LegendHeadingStyle = TextStyle(
//    fontSize = 10.sp,
//    fontWeight = FontWeight(600),
//    letterSpacing = 0.5.sp,
//    fontFamily = fontFamily
//)