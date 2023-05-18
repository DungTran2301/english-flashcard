package com.dungtran.android.core.englishflashcard.ui.core.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseListener
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.AddPhaseDialogListener

abstract class BaseDialogFragment<V : ViewDataBinding>(private val listener: AddPhaseDialogListener) : DialogFragment() {

    protected lateinit var binding: V
    private var mView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())

        if (mView != null) {
            mView
        }
        else {
            binding = DataBindingUtil.inflate(inflater, getLayoutResId(), null, false)
            binding.lifecycleOwner = this
            mView = binding.root
//            performBeforeInitView()
//            initView()
            binding.root
        }

        val dialog = Dialog(requireContext(), getDialogTheme())
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar_color)
        }

        initViews(binding.root)

        return dialog
    }

    protected abstract fun getLayoutResId(): Int

    protected open fun getDialogTheme(): Int {
        return R.style.MyDialogTheme
    }

    protected abstract fun initViews(view: View)
}