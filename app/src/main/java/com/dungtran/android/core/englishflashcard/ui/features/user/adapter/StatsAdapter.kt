package com.dungtran.android.core.englishflashcard.ui.features.user.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.model.StatsDB
import com.dungtran.android.core.englishflashcard.databinding.ItemSetBinding
import com.dungtran.android.core.englishflashcard.databinding.ItemStatsBinding
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseAdapter
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseViewHolder
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.utils.Constants

class StatsAdapter(private val context: Context) : BaseAdapter<StatsDB>(R.layout.item_stats) {


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder.binding is ItemStatsBinding) {
            val color = Constants.THEME_RES[getTheme(list[position].level)]
            holder.binding.cvBgType.setCardBackgroundColor(ContextCompat.getColor(context, color.secondaryColor))
            holder.binding.tvType.setTextColor(ContextCompat.getColor(context, color.primaryColor))
        }
        super.onBindViewHolder(holder, position)
    }

    fun getTheme(level: String): Int {
        return when (level) {
            "Beginner" -> 0
            "Intermediate" -> 1
            "Advanced" -> 2
            else -> 0
        }
    }

}