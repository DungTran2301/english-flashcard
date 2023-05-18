package com.dungtran.android.core.englishflashcard.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.utils.TimeUtils
import kotlinx.parcelize.Parcelize


data class StatsDB (
    var title: String = "",
    var studyDuration: Long = 0,
    var numberOfRightAnswers: Int = 0,
    var numberOfWrongAnswers: Int = 0,
    var level: String = "Beginner",
    var percent: String = "100%",
    var timeString: String = "",
)


fun SetDB.toStatsDB(): StatsDB {
    return StatsDB(
        title = this.title,
        studyDuration = this.studyDuration,
        numberOfRightAnswers = this.numberOfRightAnswers,
        numberOfWrongAnswers = this.numberOfWrongAnswers,
        level = getLevel(numberOfRightAnswers * 1.0f / getUnder(numberOfRightAnswers, numberOfWrongAnswers)),
        percent = "${numberOfRightAnswers * 100 / getUnder(numberOfRightAnswers, numberOfWrongAnswers)}%",
        timeString = getStudyTime(this.studyDuration)
    )
}

fun getLevel(value: Float): String {
    return when (value) {
        in 0f..0.4f -> "Beginner"
        in 0.4f..0.7f -> "Intermediate"
        in 0.7f..1f -> "Advanced"
        else -> "Beginner"
    }
}

fun getUnder(r: Int, w: Int): Int {
    return if (r + w == 0) 1 else r + w
}

fun getStudyTime(studyDuration: Long): String {
    return "Study time: ${TimeUtils.formatDuration(studyDuration)}"
}