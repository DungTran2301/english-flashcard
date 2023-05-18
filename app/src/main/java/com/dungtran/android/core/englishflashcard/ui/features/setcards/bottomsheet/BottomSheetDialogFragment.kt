package com.dungtran.android.core.englishflashcard.ui.features.setcards.bottomsheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(private val listener: OnBottomSheetClickListener) : BottomSheetDialogFragment() {
   private lateinit var binding: BottomSheetDialogBinding


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      binding = BottomSheetDialogBinding.inflate(inflater, container, false)
      if (dialog != null && dialog!!.window != null) {
         dialog!!.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.requestFeature(Window.FEATURE_NO_TITLE)
         }
      }
      initView()
      return binding.root
   }

   override fun getTheme(): Int {
      return R.style.TransparentBottomSheetDialog
   }

   private fun initView() {
      binding.tvCard.setOnClickListener {
         listener.onItemClick(1)
         dismiss()
      }

      binding.tvMultipleChoice.setOnClickListener {
         listener.onItemClick(2)
         dismiss()
      }
   }
}