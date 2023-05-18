package com.dungtran.android.core.englishflashcard.ui.first

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.ActivityFirstBinding
import com.dungtran.android.core.englishflashcard.databinding.ActivityMainBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseActivity

class FirstActivity :BaseActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun getFragmentID(): Int {
        return  R.id.navHostFragment
    }

    override fun bind() {
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun viewProcess() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

}