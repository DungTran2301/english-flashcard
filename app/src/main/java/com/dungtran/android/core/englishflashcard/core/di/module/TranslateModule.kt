package com.dungtran.android.core.englishflashcard.core.di.module

import com.dungtran.android.core.englishflashcard.utils.TranslateUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslateModule {

    @Singleton
    @Provides
    fun providesTranslateUtils() = TranslateUtils()
}