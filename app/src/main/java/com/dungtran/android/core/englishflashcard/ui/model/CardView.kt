package com.dungtran.android.core.englishflashcard.ui.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import kotlinx.parcelize.Parcelize

class CardView (
    var setID: Long = 0,
    var front: String = "", // front text
    var back: String = "", // back text
    var phonetic: String = "", // phonetic
    var vocalization: String = "", // url of sound
    var frontImage: String = "", // path to front image
    var backImage: String = "", // path to back image
    var rating: Int = 1, // if true in Quiz, +1 else -1
    var flags: Int = 0,

)  {

}

fun CardView.toCardDB(): CardDB {
    return CardDB(
        0,
        this.setID,
        this.front, // front text
        this.back, // back text
        this.phonetic,
        this.vocalization,
        this.frontImage,
        this.backImage,
        this.rating,
        this.flags
    )
}