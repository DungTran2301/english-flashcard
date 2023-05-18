package com.dungtran.android.core.englishflashcard.core.di.module

import android.content.Context
import com.dungtran.android.core.englishflashcard.utils.sound.MediaPlayerUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
//    @Provides
//    @Singleton
//    fun provideMediaPlayerUtils(@ApplicationContext context: Context): MediaPlayerUtils {
//        return MediaPlayerUtils(context)
//    }
}