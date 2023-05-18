package com.dungtran.android.core.englishflashcard.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tblLocalCard",
    foreignKeys = [
        ForeignKey(
            entity = SetDB::class,
            parentColumns = ["id"],
            childColumns = ["set_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class CardDB (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "set_id", index = true)
    var setID: Long,

    var front: String = "", // front text
    var back: String = "", // back text


    var phonetic: String = "", // phonetic
    var vocalization: String = "", // url of sound

    @ColumnInfo(name = "front_image")
    var frontImage: String = "", // path to front image
    @ColumnInfo(name = "back_image")
    var backImage: String = "", // path to back image

    var rating: Int = 1, // if true in Quiz, +1 else -1
    var flags: Int = 0,

    @Transient
    var isSelected: Boolean = false
) : Parcelable {
    constructor() : this(0, 0)

    fun reverse() {
        val f = front
        val fImage = frontImage
        this.front = back
        this.frontImage = backImage
        this.back = f
        this.backImage = fImage
    }

    fun clone(): CardDB {
        return CardDB(
            id, setID,
            front, back,
            phonetic, vocalization,
            frontImage, backImage,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is CardDB) return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + setID.hashCode()
        result = 31 * result + front.hashCode()
        result = 31 * result + back.hashCode()
        result = 31 * result + frontImage.hashCode()
        result = 31 * result + backImage.hashCode()
        result = 31 * result + rating
        result = 31 * result + flags
        result = 31 * result + isSelected.hashCode()
        return result
    }
}

fun CardDB.toCardView(): CardView {
    return CardView(
        this.setID,
        this.front, // front text
        this.back, // back text
        this.phonetic,
        this.vocalization
    )
}