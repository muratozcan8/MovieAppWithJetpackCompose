package com.obss.firstapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.obss.firstapp.R

val ubuntuMediumFont =
    FontFamily(
        listOf(
            Font(R.font.ubuntu_medium),
        ),
    )

val ubuntuBoldFont =
    FontFamily(
        listOf(
            Font(R.font.ubuntu_bold),
        ),
    )

val ubuntuLightFont =
    FontFamily(
        listOf(
            Font(R.font.ubuntu_light),
        ),
    )

val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = ubuntuMediumFont,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
    )
