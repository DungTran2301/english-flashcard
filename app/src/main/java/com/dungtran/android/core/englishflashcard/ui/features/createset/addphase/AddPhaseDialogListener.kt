package com.dungtran.android.core.englishflashcard.ui.features.createset.addphase

import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.model.CardView

interface AddPhaseDialogListener : BaseListener {
    fun onChoosePhase(cardView: CardView)
}