package com.dungtran.android.core.englishflashcard.core.data.response.dictionary

data class DictionaryResponseItem(
    val phonetic: String? = null,
    val phonetics: List<Phonetic>? = null,
    val word: String? = null
)