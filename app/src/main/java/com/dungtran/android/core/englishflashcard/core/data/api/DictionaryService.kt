package com.dungtran.android.core.englishflashcard.core.data.api

import com.dungtran.android.core.englishflashcard.core.data.response.dictionary.DictionaryResponseItem
import retrofit2.http.GET
import retrofit2.http.Path


interface DictionaryService {
    @GET("entries/{lang}/{word}")
    suspend fun getDefinition(
        @Path("lang") lang: String?,
        @Path("word") word: String?
    ): List<DictionaryResponseItem>?
}