package com.dungtran.android.core.englishflashcard.ui.features.home.adapter

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.SetView

interface HomeItemClickListener: BaseListener {
    fun onItemClick(item: SetView, position:Int)
}