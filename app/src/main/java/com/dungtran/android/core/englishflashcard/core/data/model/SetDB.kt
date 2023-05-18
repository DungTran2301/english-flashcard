package com.dungtran.android.core.englishflashcard.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tblSet")
class SetDB (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String = "",
    var count: Int = 0,
    var type: String = "",
    @ColumnInfo(name = "last_study_duration")
    var studyDuration: Long = 0,
    @ColumnInfo(name = "number_of_right_answers")
    var numberOfRightAnswers: Int = 0,
    @ColumnInfo(name = "number_of_wrong_answers")
    var numberOfWrongAnswers: Int = 0,
    var color: String = "",
    @ColumnInfo(name = "text_color")
    var textColor: String? = "",
    @ColumnInfo(name = "is_trash")
    var isTrash: Boolean = false,
    @ColumnInfo(name = "is_pinned")
    var isPinned: Boolean = false,
    var flags: Int = 0,
) : Parcelable {
    constructor() : this(0, "", 0, "", 0, 0, 0, "", "", false, false, 0)

    fun clone() = SetDB(id, title, count, type, studyDuration, numberOfRightAnswers, numberOfWrongAnswers, color, textColor)

    fun setSomeFeature(set: SetDB) {
        set.flags = set.flags or 1
    }

    fun someFeature() = flags and 1 >= 1
    fun someOtherFeature() = flags and 2 >= 1
}

fun SetDB.toSetViewModel(): SetView {
    return SetView(
        id = this.id,
        title = this.title,
        count = this.count,
        type = this.type,
        studyDuration = this.studyDuration,
        color = this.color,
        textColor = this.textColor,
        isTrash = this.isTrash,
        isPinned = this.isPinned,
        flags = this.flags
    )
}