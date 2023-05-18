package com.dungtran.android.core.englishflashcard.utils

import android.content.Context
import android.widget.Toast

object TimeUtils {
    fun formatDuration(durationInMillis: Long): String {
        val seconds = durationInMillis / 1000
        val absSeconds = kotlin.math.abs(seconds)
        val minutes = absSeconds / 60
        val hours = minutes / 60

        val hoursString = if (hours != 0L) "$hours hour" + if (hours > 1) "s" else "" else ""
        val minutesString = if (minutes != 0L) "${minutes % 60} minute" + if ((minutes % 60) > 1) "s" else "" else ""
        val secondsString = if (absSeconds < 60) "$absSeconds second" + if (absSeconds != 1L) "s" else "" else ""

        val durationString = mutableListOf<String>()
        if (hoursString.isNotEmpty()) durationString.add(hoursString)
        if (minutesString.isNotEmpty()) durationString.add(minutesString)
        if (secondsString.isNotEmpty()) durationString.add(secondsString)

        return if (durationString.isEmpty()) "0 seconds" else durationString.joinToString(", ")
    }
}