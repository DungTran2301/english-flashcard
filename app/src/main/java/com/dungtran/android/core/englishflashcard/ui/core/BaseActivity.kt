package com.dungtran.android.core.englishflashcard.ui.core

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    protected lateinit var navController: NavController
    protected lateinit var navOptionWithoutAddToBackStack: NavOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        navHostFragment =
            supportFragmentManager.findFragmentById(getFragmentID()) as NavHostFragment
        navController = navHostFragment.navController
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        navOptionWithoutAddToBackStack = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()

        viewProcess()
    }

    abstract fun getFragmentID(): Int
    abstract fun bind()
    abstract fun viewProcess()

}