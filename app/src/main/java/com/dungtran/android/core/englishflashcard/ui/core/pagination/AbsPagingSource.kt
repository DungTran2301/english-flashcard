package com.dungtran.android.core.englishflashcard.ui.core.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dungtran.android.core.englishflashcard.core.data.response.discovery.BasePagingResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


abstract class AbsPagingSource<T: Any> : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        try {
            val nextPage = params.key ?: 1
            val response = getData(nextPage)
            return LoadResult.Page(
                data = response.data,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = response.page + 1
            ).takeIf { response.data.isNotEmpty() }
                ?: LoadResult.Page(emptyList(), null, null)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    abstract suspend fun getData(page: Int): BasePagingResponse<T>
}