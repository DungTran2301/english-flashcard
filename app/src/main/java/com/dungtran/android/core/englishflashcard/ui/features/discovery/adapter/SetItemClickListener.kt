package com.dungtran.android.core.englishflashcard.ui.features.discovery.adapter

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.SetView

interface SetItemClickListener: BaseListener {
    fun onItemClick(item: SetView, position:Int)
    fun onItemLongClick(item: SetView, position:Int): Boolean
}