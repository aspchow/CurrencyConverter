package com.paypay.currencyconverter.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.paypay.currencyconverter.R

// Set of Material typography styles to start with

val notoSansFamily = FontFamily(
    Font(R.font.notosans_regular, FontWeight.Light),
    Font(R.font.notosans_regular, FontWeight.Normal),
    Font(R.font.notosans_mediumitalic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.notosans_medium, FontWeight.Medium),
    Font(R.font.notosans_bold, FontWeight.Bold)
)


val Typography = Typography(
    defaultFontFamily = notoSansFamily,
    h1 = TextStyle(color = Color.Black, fontFamily = notoSansFamily, fontWeight = FontWeight.Bold),
    h2 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    h3 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    h4 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    h5 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    h6 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    subtitle1 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    subtitle2 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    body1 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    body2 = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    button = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    caption = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
    overline = TextStyle(color = Color.Black, fontFamily = notoSansFamily),
)


val fontBold =
    TextStyle(color = Color.Black, fontFamily = notoSansFamily, fontWeight = FontWeight.Bold)

val fontRegular =
    TextStyle(color = Color.Black, fontFamily = notoSansFamily, fontWeight = FontWeight.Normal)
