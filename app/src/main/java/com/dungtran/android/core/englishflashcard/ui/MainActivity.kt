package com.dungtran.android.core.englishflashcard.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.databinding.ActivityMainBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseActivity
import com.dungtran.android.core.englishflashcard.utils.listener.ControlPanelListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var setRepository: SetRepository

    override fun getFragmentID(): Int {
        return R.id.navContainerViewMain
    }

    override fun bind() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun viewProcess() {
        binding.bottomNavView.background = ContextCompat.getDrawable(this, R.drawable.bg_bottom_nav)
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navigateToFragment(R.id.homeFragment, false)
                    true
                }
                R.id.discoveryFragment -> {
                    navigateToFragment(R.id.discoveryFragment, false)
                    true
                }
                R.id.userFragment -> {
                    navigateToFragment(R.id.userFragment, false)
                    true
                }
                R.id.settingFragment -> {
                    navigateToFragment(R.id.settingFragment, false)
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            if (navDestination.id in fragmentList) {
                binding.bottomNavView.visibility = View.VISIBLE
                binding.bottomNavView.menu.findItem(navDestination.id).isChecked = true
            }
            else {
                binding.bottomNavView.visibility = View.GONE
            }
        }
    }

    private fun navigateToFragment(fragmentId: Int, isAddToBackStack: Boolean = true) {
        if (isAddToBackStack) {
            navController.navigate(fragmentId)
        }
        else {
            navController.navigate(fragmentId)
        }
    }

    fun updateStatsInfo(setId: Long, duration: Long, countRight: Int = 0, countWrong: Int = 0) {
        lifecycleScope.launch(Dispatchers.IO) {
            setRepository.saveSetStatsInfo(setId, duration, countRight, countWrong)
        }
    }

    companion object {
        private val fragmentList = listOf(
            R.id.homeFragment,
            R.id.discoveryFragment,
            R.id.settingFragment,
            R.id.userFragment,
        )
    }

}