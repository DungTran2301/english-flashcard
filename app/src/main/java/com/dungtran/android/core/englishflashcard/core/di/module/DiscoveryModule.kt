package com.dungtran.android.core.englishflashcard.core.di.module

import com.dungtran.android.core.englishflashcard.core.data.api.DictionaryService
import com.dungtran.android.core.englishflashcard.core.data.api.DiscoveryService
import com.dungtran.android.core.englishflashcard.core.data.repository.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiscoveryModule {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    @Singleton
    @Provides
    @Named("discovery")
    fun provideRetrofitForDiscovery(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideDiscoveryService(@Named("discovery") retrofit: Retrofit): DiscoveryService = retrofit.create(DiscoveryService::class.java)

}