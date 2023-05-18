package com.dungtran.android.core.englishflashcard.ui.features.setcards.bottomsheet

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView

interface OnBottomSheetClickListener: BaseListener {
    fun onItemClick(position:Int)
}