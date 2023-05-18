package com.dungtran.android.core.englishflashcard.ui.core.viewstate

sealed class ViewState<T>() {
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error<T>(val errorCode: String, val errorMessage: String) : ViewState<T>()
}