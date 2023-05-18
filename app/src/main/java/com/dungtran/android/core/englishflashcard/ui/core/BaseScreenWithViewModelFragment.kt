package com.dungtran.android.core.englishflashcard.ui.core

import androidx.databinding.ViewDataBinding

abstract class BaseScreenWithViewModelFragment<V : ViewDataBinding> : BaseScreenFragment<V>(){

    override fun performBeforeInitView() {
        initViewModel()
    }

    abstract fun initViewModel()

}