package com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView

interface OnPhaseClickListener: BaseListener {
    fun onItemClick(item: CardView, position:Int)
    fun onSpeakerClick(item: CardView)
}