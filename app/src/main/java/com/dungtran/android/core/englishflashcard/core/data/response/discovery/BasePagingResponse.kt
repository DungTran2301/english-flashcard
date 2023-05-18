package com.dungtran.android.core.englishflashcard.core.data.response.discovery

data class BasePagingResponse<T: Any>(
    var `data`: List<T>,
    var page: Int
)