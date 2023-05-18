package com.dungtran.android.core.englishflashcard.ui.first

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.FragmentSplashBinding
import com.dungtran.android.core.englishflashcard.ui.MainActivity
import com.dungtran.android.core.englishflashcard.ui.core.BaseFragment
import com.dungtran.android.core.englishflashcard.utils.Prefs

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private val handler = Handler(Looper.myLooper()!!)
    private val runnable = Runnable {
        if (Prefs.isLogin(requireContext())) {
            requireActivity().startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //nothing
                }
            })
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 3000)
    }

    override fun initView() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //add flag to show fullscreen in splash screen
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onStop() {
        super.onStop()
        //remove flag to show normal mode when navigate to next screen from splash screen
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun performBeforeInitView() {

    }
}