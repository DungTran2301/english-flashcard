package com.dungtran.android.core.englishflashcard.ui.features.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.android.core.englishflashcard.core.data.model.*
import com.dungtran.android.core.englishflashcard.core.data.repository.CardRepository
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.ui.model.toCardDB
import com.dungtran.android.core.englishflashcard.utils.StringUtils
import com.dungtran.android.core.englishflashcard.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.platform.android.AndroidLogHandler.getLevel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val setRepository: SetRepository
    ): ViewModel() {

    private val _myData = MutableStateFlow<DataResponse<List<StatsDB>>>(DataResponse.DataIdle())
    val myData: StateFlow<DataResponse<List<StatsDB>>> = _myData.asStateFlow()

    val isEmpty: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Error }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isLoading: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Loading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val totalTime: StateFlow<String> = _myData.map {
        when (it.loadingStatus) {
            LoadingStatus.Success -> {
                val body = (it as DataResponse.DataSuccess).body

                "Total time: " + TimeUtils.formatDuration(body.sumOf { stats: StatsDB -> stats.studyDuration })
            }
            else -> {
                "Total time: 0 hour"
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "Total time: 0 hour")

    val level: StateFlow<String> = _myData.map {
        when (it.loadingStatus) {
            LoadingStatus.Success -> {
                val body = (it as DataResponse.DataSuccess).body
                val r = body.sumOf { stats: StatsDB -> stats.numberOfRightAnswers }
                val w = body.sumOf { stats: StatsDB -> stats.numberOfWrongAnswers }
                StringUtils.getLevel(r * 1.0f / getUnder(r, r))
            }
            else -> {
                "Beginner"
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "Beginner")

    val percentString: StateFlow<String> = _myData.map {
        when (it.loadingStatus) {
            LoadingStatus.Success -> {
                val body = (it as DataResponse.DataSuccess).body
                val r = body.sumOf { stats: StatsDB -> stats.numberOfRightAnswers }
                val w = body.sumOf { stats: StatsDB -> stats.numberOfWrongAnswers }
                "${r * 100 / getUnder(r, w)}%"
            }
            else -> {
                "100%"
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "100%")


    val percent: StateFlow<Int> = _myData.map {
        when (it.loadingStatus) {
            LoadingStatus.Success -> {
                val body = (it as DataResponse.DataSuccess).body
                val r = body.sumOf { stats: StatsDB -> stats.numberOfRightAnswers }
                val w = body.sumOf { stats: StatsDB -> stats.numberOfWrongAnswers }
                r * 100 / getUnder(r, w)
            }
            else -> {
                100
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 100)

    init {
        viewModelScope.launch {
            _myData.value = DataResponse.DataLoading(LoadingStatus.Loading)
            val res = setRepository.getSetStats()
            if (res.isSuccess) {
                val list = res.getOrNull()
                _myData.value = DataResponse.DataSuccess(list ?: List(5) { StatsDB() })
            } else {
                _myData.value = DataResponse.DataSuccess(List(20) { StatsDB() })
            }
        }
    }

}
