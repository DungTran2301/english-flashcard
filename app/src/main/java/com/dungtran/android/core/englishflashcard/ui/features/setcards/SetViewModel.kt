package com.dungtran.android.core.englishflashcard.ui.features.setcards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import com.dungtran.android.core.englishflashcard.core.data.model.toCardView
import com.dungtran.android.core.englishflashcard.core.data.repository.CardRepository
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.ui.model.toCardDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val setRepository: SetRepository
    ): ViewModel() {

    var setView: SetView = SetView()
    private var isRemote: Boolean = false

    private val _myData = MutableStateFlow<DataResponse<List<CardView>>>(DataResponse.DataIdle())
    val myData: StateFlow<DataResponse<List<CardView>>> = _myData.asStateFlow()

    var savingStatus: MutableLiveData<LoadingStatus> = MutableLiveData(LoadingStatus.Idle)

    val isEmpty: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Error }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isLoading: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Loading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val numberOfPhases: StateFlow<String> = _myData.map {
        when (it.loadingStatus) {
            LoadingStatus.Success -> {
                val body = (it as DataResponse.DataSuccess).body
                "Number of phases " + body.size
            }
            else -> {
                "Number of phases 0"
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "Number of phases 0")

    fun setSet(set: SetView, isRemote: Int = 0) {
        setView = set
        this.isRemote = isRemote == 1
    }

    fun getData() {
        viewModelScope.launch {
            _myData.value = DataResponse.DataLoading(LoadingStatus.Loading)
            val res = cardRepository.getCardsBySetId(setView.id, isRemote)
            if (res.isSuccess) {
                val list = res.getOrNull()
                val convertedList =  list?.map { cardDb: CardDB -> cardDb.toCardView() }
                _myData.value = DataResponse.DataSuccess(convertedList ?: List(5) { CardView() })
            }
            else {
                _myData.value = DataResponse.DataSuccess(List(20) { CardView() })
            }
        }
    }

    fun addCard(card: CardView) {
        viewModelScope.launch {
            savingStatus.postValue(LoadingStatus.Loading)
            card.setID = setView.id
            val addCard= async { cardRepository.insert(card.toCardDB()) }
            val addRes = addCard.await()
            if (addRes.isSuccess) {
                getData()
                savingStatus.postValue(LoadingStatus.Success)
            }
            else {
                savingStatus.postValue(LoadingStatus.Error)
            }
        }
    }

    fun updateStatsInfo(duration: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setRepository.saveSetStatsInfo(setView.id, duration)
        }
    }
}
