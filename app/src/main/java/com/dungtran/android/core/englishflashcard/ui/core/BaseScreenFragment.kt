package com.dungtran.android.core.englishflashcard.ui.core

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.dungtran.android.core.englishflashcard.ui.MainActivity

abstract class BaseScreenFragment<V : ViewDataBinding> : BaseFragment<V>() {
    protected lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (requireActivity() as MainActivity)
    }
}