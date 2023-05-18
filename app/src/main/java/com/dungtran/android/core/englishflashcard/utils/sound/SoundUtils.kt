package com.dungtran.android.core.englishflashcard.utils.sound

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.SoundPool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SoundUtils constructor(val context: Context) {
    private var soundId = -1
    private val soundPool: SoundPool
    private var curStreamId = -1

    init {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        soundPool = SoundPool.Builder().setAudioAttributes(attributes).build()

    }

    fun play() {
        if (soundId >= 0) {
            curStreamId = soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }

    }

    fun stop() {
        if (curStreamId >= 0) {
            soundPool.stop(curStreamId)
        }
    }

    suspend fun loadSound(url: String): Boolean = withContext(Dispatchers.Default) {
        if (soundId >= 0) {
            soundPool.unload(soundId)
        }
        val afd: AssetFileDescriptor = context.assets.openFd(url)
        soundId = soundPool.load(afd, 1)
        true
    }

    fun destroy() {
        if (curStreamId >= 0) {
            soundPool.stop(curStreamId)
        }
        soundPool.unload(soundId)
        soundPool.release()

    }

}