package com.dungtran.android.core.englishflashcard.ui.features.study.card.adapter

import android.view.View
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.ItemCardBinding
import com.dungtran.android.core.englishflashcard.ui.core.platform.FlipAnimation
import com.dungtran.android.core.englishflashcard.ui.core.platform.LONG_ANIMATION
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseAdapter
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseViewHolder
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.utils.Constants

class StudyAdapter : BaseAdapter<CardView>(R.layout.item_card) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        (holder.binding as ItemCardBinding).apply {
            val color = Constants.randomThumbRes()
            ivBackground.setImageResource(color)
            cvCard.setOnClickListener {
                val anim = FlipAnimation(front, back, LONG_ANIMATION.toInt())
                if (back.visibility == View.VISIBLE) anim.reverse()
                root.startAnimation(anim)
            }
        }
    }
}