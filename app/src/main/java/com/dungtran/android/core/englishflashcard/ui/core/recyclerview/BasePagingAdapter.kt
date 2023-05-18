package com.dungtran.android.core.englishflashcard.ui.core.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dungtran.android.core.englishflashcard.BR
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import java.util.*

abstract class BasePagingAdapter(
    private val layout: Int
) : PagingDataAdapter<SetView, BaseViewHolder>(SetComparator) {

    private lateinit var inflater: LayoutInflater
    var listener: BaseListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            layout,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.binding.apply {
            setVariable(BR.item, getItem(position))
            setVariable(BR.itemPosition, position)
            setVariable(BR.itemListener, listener)
//            val context = root.context as LifecycleOwner
//            lifecycleOwner = context
            executePendingBindings()
        }
    }


    object SetComparator : DiffUtil.ItemCallback<SetView>() {
        override fun areItemsTheSame(oldItem: SetView, newItem: SetView): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SetView, newItem: SetView): Boolean {
            return oldItem.title == newItem.title
        }
    }

}
