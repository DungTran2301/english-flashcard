package com.dungtran.android.core.englishflashcard.ui.features.study.multiplechoice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import com.dungtran.android.core.englishflashcard.core.data.model.toCardView
import com.dungtran.android.core.englishflashcard.core.data.repository.CardRepository
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.ui.core.viewstate.ViewState
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.ui.model.toCardDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MultipleChoiceViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val setRepository: SetRepository
    ): ViewModel() {

    var setView: SetView = SetView()
    private var isRemote: Boolean = false

    var currentQuestion: Int = -1

    private val _myData = MutableStateFlow<DataResponse<List<MultipleChoiceView>>>(DataResponse.DataIdle())
    val myData: StateFlow<DataResponse<List<MultipleChoiceView>>> = _myData.asStateFlow()

    private val _viewState = MutableStateFlow<ViewState<MultipleChoiceView>>(ViewState.Error("", ""))
    val viewState: StateFlow<ViewState<MultipleChoiceView>> = _viewState.asStateFlow()

    val isEmpty: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Error }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isLoading: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Loading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val headerTitle: MutableLiveData<String> = MutableLiveData("")
    val progress: MutableLiveData<Int> = MutableLiveData(0)

    fun setSet(set: SetView, isRemote: Int = 0) {
        setView = set
        this.isRemote = isRemote == 1
    }

    fun nextQuestion() {
        currentQuestion++
        if (_myData.value.loadingStatus == LoadingStatus.Success) {
            val data = (_myData.value as DataResponse.DataSuccess).body
            if (currentQuestion >= data.size) {
                _viewState.value = ViewState.Error("401", "The end of question")
            }
            else {
                _viewState.value = ViewState.Success(data[currentQuestion])
                headerTitle.value = "Question ${currentQuestion + 1} / ${data.size}"
                progress.value = (currentQuestion + 1) * 100 / data.size
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            _myData.value = DataResponse.DataLoading(LoadingStatus.Loading)
            val res = cardRepository.getCardsBySetId(setView.id, isRemote)
            if (res.isSuccess) {
                val list = res.getOrNull()
                val convertedList =  list?.map { cardDb: CardDB ->
                    val correctIndex = Random.nextInt(1, 5)
                    val answerList = list.minus(cardDb)
                        .shuffled()
                        .take(3).toMutableList()

                    answerList.add(correctIndex - 1, cardDb)
                    val view = MultipleChoiceView(
                        cardDb.back,
                        answerList[0].front,
                        answerList[1].front,
                        answerList[2].front,
                        answerList[3].front,
                        correctIndex
                    )
                    view
                }
                if (convertedList != null && convertedList.isNotEmpty()) {
                    _myData.value = DataResponse.DataSuccess(convertedList)
                    currentQuestion = -1
                    nextQuestion()
                }
                else {
                    _myData.value = DataResponse.DataError()
                }
            }
            else {
                _myData.value = DataResponse.DataError()
            }
        }
    }

    fun updateStatsInfo(duration: Long, countRight: Int, countWrong: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            setRepository.saveSetStatsInfo(setView.id, duration, countRight, countWrong)
        }
    }

}
