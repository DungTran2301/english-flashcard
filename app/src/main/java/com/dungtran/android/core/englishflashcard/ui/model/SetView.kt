package com.dungtran.android.core.englishflashcard.ui.model

import android.os.Parcelable
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeItem
import kotlinx.parcelize.Parcelize
import okhttp3.internal.lowercase

@Parcelize
class SetView (
    var id: Long = 0L,
    var title: String = "",
    var count: Int = 0,
    var type: String = "",
    var studyDuration: Long = 0,
    var color: String = "",
    var textColor: String? = "",
    var isTrash: Boolean = false,
    var isPinned: Boolean = false,
    var flags: Int = 0,
    var numberOfTerms: Int = 0,
) : Parcelable {

    var typeItem: TypeItem = TypeItem("Other", R.drawable.banner_other)
    fun getNumberOfTermsString(): String = "$count thuật ngữ"

    fun getItemType(): TypeItem {
        return when {
            this.type.lowercase().contains("toeic", true) -> TypeItem("Toeic", R.drawable.banner_education)
            this.type.lowercase().contains("work", true) -> TypeItem("Work", R.drawable.banner_work)
            this.type.lowercase().contains("culture", true) -> TypeItem("Culture", R.drawable.banner_food_drink)
            this.type.lowercase().contains("music", true) -> TypeItem("Music and film", R.drawable.banner_music)
            this.type.lowercase().contains("relationship", true) -> TypeItem("Relationship", R.drawable.banner_relationship)
            this.type.lowercase().contains("travel", true) -> TypeItem("Travel", R.drawable.banner_travel)
            this.type.lowercase().contains("other", true) -> TypeItem("Other", R.drawable.banner_other)
            else -> TypeItem("Other", R.drawable.banner_other)
        }
    }
}

fun SetView.toSetDBToSave(): SetDB {
    return SetDB(
        id = 0,
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