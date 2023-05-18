package com.dungtran.android.core.englishflashcard.utils

import com.dungtran.android.core.englishflashcard.R
import kotlin.random.Random

object Constants {
    var currentColor = 0
    val THUMB_RES = listOf (
        R.drawable.thumb_1,
        R.drawable.thumb_2,
        R.drawable.thumb_3,
        R.drawable.thumb_4,
        R.drawable.thumb_5,
        R.drawable.thumb_6,
        R.drawable.thumb_7,
        R.drawable.thumb_8,
    )

    fun randomThumbRes(): Int {
        while (true) {
            val colorIndex = Random.nextInt(0, 7)
            if (colorIndex != currentColor) {
                currentColor = colorIndex
                return Constants.THUMB_RES[colorIndex]
            }
        }
    }

    val THEME_RES = listOf (
        ColorTheme(R.color.green_dark_theme, R.color.green_light_theme),

        ColorTheme(R.color.blue_dark_theme, R.color.blue_light_theme),

        ColorTheme(R.color.red_dark_theme, R.color.red_light_theme),
    )
}

data class ColorTheme(
    val primaryColor: Int,
    val secondaryColor: Int,
)