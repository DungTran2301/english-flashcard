package com.dungtran.android.core.englishflashcard.utils.listener


interface ControlPanelListener {
    fun onPauseSound()
    fun onDetachFragment()
    fun isPlaying() : Boolean
    fun onReset()
    fun onPlaySound(url: String)
}