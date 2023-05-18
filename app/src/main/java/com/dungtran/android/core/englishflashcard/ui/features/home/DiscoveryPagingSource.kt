package com.dungtran.android.core.englishflashcard.ui.features.home

import com.dungtran.android.core.englishflashcard.core.data.api.DiscoveryService
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.response.discovery.BasePagingResponse
import com.dungtran.android.core.englishflashcard.ui.core.pagination.AbsPagingSource
import com.dungtran.android.core.englishflashcard.ui.model.SetView

class DiscoveryPagingSource(private val callBack: suspend (Int) -> BasePagingResponse<SetView> ): AbsPagingSource<SetView>() {
    override suspend fun getData(page: Int): BasePagingResponse<SetView> {
        return callBack(page)
    }
}