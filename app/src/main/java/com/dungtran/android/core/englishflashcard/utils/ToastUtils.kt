package com.dungtran.android.core.englishflashcard.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}