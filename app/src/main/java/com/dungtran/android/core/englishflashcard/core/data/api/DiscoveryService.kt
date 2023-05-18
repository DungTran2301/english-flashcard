package com.dungtran.android.core.englishflashcard.core.data.api

import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.response.dictionary.DictionaryResponseItem
import com.dungtran.android.core.englishflashcard.core.data.response.discovery.BasePagingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DiscoveryService {
    @GET("phase/")
    suspend fun getSet(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): BasePagingResponse<SetDB>?

    @GET("phase/setcard/detail/{id}")
    suspend fun getCardsBySet(
        @Path("id") id: Long,
    ): List<CardDB>?
}