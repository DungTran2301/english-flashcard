package com.dungtran.android.core.englishflashcard.utils.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.dungtran.android.core.englishflashcard.utils.ToastUtils

class MediaPlayerUtils() {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(url: String) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            } else {
                mediaPlayer?.reset()
            }
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
            }
            mediaPlayer?.setOnErrorListener { _, _, _ ->
                false
            }
            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            // Handle any exceptions that may occur during preparation
        }
    }
}