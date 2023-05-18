package com.dungtran.android.core.englishflashcard.ui.features.study.multiplechoice

data class MultipleChoiceView (
    val question: String,
    val answerA: String,
    val answerB: String,
    val answerC: String,
    val answerD: String,
    val correctAnswer: Int,
) {
    fun getCorrectAnswer(): String {
        return when (correctAnswer) {
            1 -> answerA
            2 -> answerB
            3 -> answerC
            else -> answerD
        }
    }
}