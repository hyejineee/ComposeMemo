package com.example.composememoapp.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.composememoapp.R

// Set of Material typography styles to start with
//val Typography = Typography(
//    body1 = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp
//    )
/* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
//)


private val regular = Font(R.font.s_core_dream_regular, FontWeight.Normal)
private val medium = Font(R.font.s_core_dream_medium, FontWeight.Medium)
private val extraBold = Font(R.font.reco_extra_bold, FontWeight.ExtraBold)

private val appFontFamily = FontFamily(regular, medium, extraBold)

val appFont = Typography(
    defaultFontFamily = appFontFamily,
)
