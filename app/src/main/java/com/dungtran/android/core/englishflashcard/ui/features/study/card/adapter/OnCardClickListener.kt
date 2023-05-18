package com.dungtran.android.core.englishflashcard.ui.features.study.card.adapter

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.CardView

interface OnCardClickListener: BaseListener {
    fun onItemClick(item: CardView, position:Int)
    fun onSpeakerClick(item: CardView)
}