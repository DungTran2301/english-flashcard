package com.dungtran.android.core.englishflashcard.ui.features.createset.addphase

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.translation.Translator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.model.toSetViewModel
import com.dungtran.android.core.englishflashcard.core.data.repository.DictionaryRepository
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.core.data.response.dictionary.DictionaryResponseItem
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import com.dungtran.android.core.englishflashcard.utils.TranslateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class AddPhaseViewModel @Inject constructor(val repository: DictionaryRepository): ViewModel() {

//    private val _myData = MutableStateFlow<DataResponse<CardView>>(DataResponse.DataIdle())
//    val myData: StateFlow<DataResponse<CardView>> = _myData.asStateFlow()
    @Inject
    lateinit var translateUtils: TranslateUtils

    private val _myData = MutableStateFlow<DataResponse<List<CardView>>>(DataResponse.DataIdle())
    val myData: StateFlow<DataResponse<List<CardView>>> = _myData.asStateFlow()


    val isEmpty: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Error }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isLoading: StateFlow<Boolean> = _myData.map { it.loadingStatus == LoadingStatus.Loading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {

    }

    fun getWordInfo(keyWord: String) {
        viewModelScope.launch {
            _myData.value = DataResponse.DataLoading(LoadingStatus.Loading)
            try {
                repository.getDictionaryOfWord(word = keyWord)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        _myData.value = DataResponse.DataError()
                    }
                    .collect {
                        if (it == null) {
                            _myData.value = DataResponse.DataError()
                        }
                        else {
                            _myData.value = DataResponse.DataSuccess(getDataFromResponse(it))
                        }
                    }
            } catch (ex: Exception) {
                _myData.value = DataResponse.DataError()
            }

        }
    }

    private suspend fun getDataFromResponse(list: List<DictionaryResponseItem>): List<CardView> = withContext(Dispatchers.IO) {
        val res = mutableListOf<CardView>()
        var text = ""
        var front = ""
        list.forEach { item ->
            front = item.word ?: "Default"
            if (item.phonetic != null) {
                text = item.phonetic
            }
            item.phonetics?.let { it ->
                it.forEach { phonetic ->
                    val mCardView = CardView()
                    mCardView.front = front
                    val translatedText = async { translateUtils.translate(front) }
                    mCardView.back = translatedText.await()
                    mCardView.phonetic = phonetic.text ?: text
                    mCardView.vocalization = phonetic.audio ?: ""
                    if (mCardView.phonetic.isNotEmpty() && mCardView.vocalization.isNotEmpty()) {
                        res.add(mCardView)
                    }


                }
            }
        }
        val distinctList = res.distinctBy { Triple(it.front, it.back, it.phonetic) }
        distinctList
    }
}
