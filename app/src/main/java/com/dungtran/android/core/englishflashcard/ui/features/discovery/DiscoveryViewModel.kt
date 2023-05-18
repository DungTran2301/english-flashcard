package com.dungtran.android.core.englishflashcard.ui.features.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.model.toCardView
import com.dungtran.android.core.englishflashcard.core.data.model.toSetViewModel
import com.dungtran.android.core.englishflashcard.core.data.repository.CardRepository
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.core.data.response.discovery.BasePagingResponse
import com.dungtran.android.core.englishflashcard.ui.features.home.DiscoveryPagingSource
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.ui.model.toCardDB
import com.dungtran.android.core.englishflashcard.ui.model.toSetDBToSave
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val setRepository: SetRepository,
    private val cardRepository: CardRepository
    ) : ViewModel() {

    private val _myData = MutableStateFlow<DataResponse<List<SetView>>>(DataResponse.DataIdle())
    val myData: StateFlow<DataResponse<List<SetView>>> = _myData.asStateFlow()

    var savingStatus: MutableLiveData<LoadingStatus> = MutableLiveData(LoadingStatus.Idle)

    val movies: Flow<PagingData<SetView>> = Pager(PagingConfig(
        pageSize = 10,
        initialLoadSize = 1,
        prefetchDistance = 1,
        enablePlaceholders = true
    )) {
        DiscoveryPagingSource { page ->
            val response: BasePagingResponse<SetView> = BasePagingResponse(
                page = 1,
                data = emptyList<SetView>()
            )
            val res = withContext(Dispatchers.IO) {
                setRepository.getRemoteSet(page, PAGE_SIZE)
            }
            if (res.isSuccess) {
                val r = res.getOrNull()?: throw Exception("Fail")
                response.page = r.page
                response.data = r.data.map { setDB: SetDB -> setDB.toSetViewModel() }
            } else {
                throw Exception("Fail")
            }
            response
        }
    }.flow
        .cachedIn(viewModelScope)

    fun saveSet(set: SetView) {
        viewModelScope.launch(Dispatchers.IO) {
            savingStatus.postValue(LoadingStatus.Loading)
            try {
                val res = cardRepository.getCardsBySetId(set.id, true)

                if (res.isSuccess) {
                    val cards = res.getOrNull() ?: emptyList()
                    val saveRes = setRepository.saveSetFromServer(set.toSetDBToSave(), cards)
                    if (saveRes.isSuccess) {
                        savingStatus.postValue(LoadingStatus.Success)
                    } else {
                        savingStatus.postValue(LoadingStatus.Error)
                    }
                } else {
                    savingStatus.postValue(LoadingStatus.Error)
                }
            }
            catch (ex: Exception) {
                savingStatus.postValue(LoadingStatus.Error)
            }
        }
    }


//    val isEmpty: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Error }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
//
//    val isLoading: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Loading }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
//
//    init {
//
//        viewModelScope.launch {
//            _myData.value = DataResponse.DataLoading(LoadingStatus.Loading)
////            delay(1500)
////            _myData.value = DataResponse.DataSuccess(List(15) { SetView() })
//            val res = setRepository.getAllSet(isRemote = true)
//            if (res.isSuccess) {
//                val list = res.getOrNull()
//                val convertedList = list?.map { setDB: SetDB -> setDB.toSetViewModel() }
//                _myData.value = DataResponse.DataSuccess(convertedList ?: List(5) { SetView() })
//            } else {
//                _myData.value = DataResponse.DataSuccess(List(20) { SetView() })
//            }
//        }
//    }


    companion object {
        const val PAGE_SIZE = 10
    }
}
