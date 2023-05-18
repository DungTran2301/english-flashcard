package com.dungtran.android.core.englishflashcard.ui.features.createset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.repository.CardRepository
import com.dungtran.android.core.englishflashcard.core.data.repository.SetRepository
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.toCardDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSetViewModel @Inject constructor(
    private val setRepository: SetRepository,
    private val cardRepository: CardRepository
    ): ViewModel() {

    private val _mListCard = MutableStateFlow<List<CardView>>(emptyList())
    val mListCard: StateFlow<List<CardView>> = _mListCard.asStateFlow()

    private val cards = mutableListOf<CardView>()

    var savingStatus: MutableLiveData<LoadingStatus> = MutableLiveData(LoadingStatus.Idle)

    val isEmpty: StateFlow<Boolean> = _mListCard.map { it.isEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {

    }

    fun addCard(cardView: CardView) {
        cards.add(cardView)
        _mListCard.value = emptyList()
        _mListCard.value = cards
    }

    fun save(set: SetDB) {
        viewModelScope.launch(Dispatchers.IO) {
            savingStatus.postValue(LoadingStatus.Loading)
            if (_mListCard.value.isEmpty()) {
                savingStatus.postValue(LoadingStatus.Error)
            }
            else {
                set.count = _mListCard.value.size
                val saveSet = async { setRepository.insert(set) }
                val res = saveSet.await()
                if (res.isSuccess) {
                    val setId = res.getOrNull()
                    if (setId != null) {
                        val listCardDB = _mListCard.value.map { cardView ->
                            cardView.setID = setId
                            cardView.toCardDB()
                        }

                        val cardInsertRes = cardRepository.insert(listCardDB)
                        if (cardInsertRes.isSuccess) {
                            savingStatus.postValue(LoadingStatus.Success)
                        } else {
                            savingStatus.postValue(LoadingStatus.Error)
                        }
                    } else {
                        savingStatus.postValue(LoadingStatus.Error)
                    }
                } else {
                    savingStatus.postValue(LoadingStatus.Error)
                }
            }
        }
    }
}
