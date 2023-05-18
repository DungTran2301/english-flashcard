package com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.SetView

interface TypeItemClickListener: BaseListener {
    fun onItemClick(item: TypeItem, position:Int)
}