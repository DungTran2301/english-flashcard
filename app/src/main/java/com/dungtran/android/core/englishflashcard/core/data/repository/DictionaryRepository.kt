package com.dungtran.android.core.englishflashcard.core.data.repository

import com.dungtran.android.core.englishflashcard.core.data.api.DictionaryService
import kotlinx.coroutines.flow.flow

class DictionaryRepository(private val dictionaryService: DictionaryService) {

    fun getDictionaryOfWord(lang: String = "en", word: String) = flow {
        emit(dictionaryService.getDefinition(lang, word))
    }

}