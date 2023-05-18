package com.dungtran.android.core.englishflashcard.ui.features.home.adapter

import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.ItemSetBinding
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseAdapter
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseViewHolder
import com.dungtran.android.core.englishflashcard.ui.model.SetView

class HomeAdapter : BaseAdapter<SetView>(R.layout.item_set) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder.binding is ItemSetBinding) {
            holder.binding.flCardSet.setBackgroundResource(list[position].getItemType().banner)
        }
        super.onBindViewHolder(holder, position)
    }

    fun getCurrentItem(position: Int): SetView {
        return list[position]
    }

    fun remove(position: Int): Boolean {
        return try {
            list.removeAt(position)
            true
        } catch (ex: Exception){
            false
        }
    }
    fun add(position: Int, setView: SetView): Boolean {
        return try {
            list.add(position, setView)
            true
        } catch (ex: Exception){
            false
        }
    }
}