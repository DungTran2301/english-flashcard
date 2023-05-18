package com.dungtran.android.core.englishflashcard.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.model.toSetViewModel
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val setRepository: SetRepository
    ) : ViewModel() {

    private val _myData = MutableStateFlow<DataResponse<List<SetView>>>(DataResponse.DataIdle())
    val myData: StateFlow<DataResponse<List<SetView>>> = _myData.asStateFlow()


    val isEmpty: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Error }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isLoading: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Loading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {

        viewModelScope.launch {
            _myData.value = DataResponse.DataLoading(LoadingStatus.Loading)
            val res = setRepository.getAllSet()
            if (res.isSuccess) {
                val list = res.getOrNull()
                val convertedList = list?.map { setDB: SetDB -> setDB.toSetViewModel() }
                _myData.value = DataResponse.DataSuccess(convertedList ?: List(5) { SetView() })
            } else {
                _myData.value = DataResponse.DataSuccess(List(20) { SetView() })
            }
        }
    }

    fun removeSet(setView: SetView){
        viewModelScope.launch(Dispatchers.IO) {
            setRepository.deleteSetId(setView.id)
        }
    }
}
