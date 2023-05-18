package com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.ui.features.createset.CreateSetFragment

object TypeDialogHelper {

    private fun showChooseTypeDialog(context: Context, adapter: TypeAdapter) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choose_type)
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val dialogWidth = (screenWidth * 0.8).toInt()
        val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(dialogWidth, dialogHeight)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.dialog_recycler_view)
        recyclerView.adapter = adapter

//            TypeAdapter().apply {
//            listener = object : TypeItemClickListener {
//                override fun onItemClick(item: TypeItem, position: Int) {
//                    binding.tvType.text = item.text
//                    dialog.dismiss()
//                }
//            }
//            submit(CreateSetFragment.items)
//        }

        val cancelButton = dialog.findViewById<Button>(R.id.dialog_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}