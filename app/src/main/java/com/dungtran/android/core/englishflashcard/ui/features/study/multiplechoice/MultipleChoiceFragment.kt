package com.dungtran.android.core.englishflashcard.ui.features.study.multiplechoice

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentMultipleChoiceBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.core.viewstate.ViewState
import com.dungtran.android.core.englishflashcard.ui.features.createset.CreateSetFragment
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeAdapter
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeItem
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeItemClickListener
import com.dungtran.android.core.englishflashcard.ui.features.setcards.SetViewModel
import com.dungtran.android.core.englishflashcard.ui.features.study.card.StudyFragmentArgs
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MultipleChoiceFragment : BaseScreenWithViewModelFragment<FragmentMultipleChoiceBinding>() {
    private val mViewModel: MultipleChoiceViewModel by viewModels()

    private val args: MultipleChoiceFragmentArgs by navArgs()

    private lateinit var viewData: MultipleChoiceView

    private var countRight: Int = 0
    private var countWrong: Int = 0
    private var startTime: Long = 0
    private var duration: Long = 0

    override fun getLayout(): Int {
        return R.layout.fragment_multiple_choice
    }

    override fun initView() {
        binding.apply {
            viewModel = mViewModel
            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            answerA.answer.setOnClickListener {
                if (viewData.correctAnswer == 1) {
                    onRightAnswer()
                }
                else {
                    onWrongAnswer(viewData.answerA)
                }
            }

            answerB.answer.setOnClickListener {
                if (viewData.correctAnswer == 2) {
                    onRightAnswer()
                }
                else {
                    onWrongAnswer(viewData.answerB)
                }
            }

            answerC.answer.setOnClickListener {
                if (viewData.correctAnswer == 3) {
                    onRightAnswer()
                }
                else {
                    onWrongAnswer(viewData.answerC)
                }
            }
            answerD.answer.setOnClickListener {
                if (viewData.correctAnswer == 4) {
                    onRightAnswer()
                }
                else {
                    onWrongAnswer(viewData.answerD)
                }
            }
        }

        observe()
    }

    override fun initViewModel() {
        mViewModel.setSet(args.set, args.isRemote)
        mViewModel.getData()
    }

    private fun observe() {
        lifecycleScope.launch {
            mViewModel.viewState.collect {
                when (it) {
                    is ViewState.Success -> {
                        viewData = it.data

                        binding.apply {
                            tvQuestion.text = viewData.question
                            answerA.tvAnswer.text = viewData.answerA
                            answerB.tvAnswer.text = viewData.answerB
                            answerC.tvAnswer.text = viewData.answerC
                            answerD.tvAnswer.text = viewData.answerD
                        }
                    }
                    is ViewState.Error -> {
                        when (it.errorCode) {
                            "401" -> {
                                showEndDialog()
                            }
                            else -> {

                            }
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun showEndDialog() {
        showCongratsDialog()
    }

    private fun onWrongAnswer(userAnswer: String) {
        countWrong++
        val context = requireContext()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_wrong_answer)
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val dialogWidth = (screenWidth * 0.8).toInt()
        val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(dialogWidth, dialogHeight)
        val tv = dialog.findViewById<TextView>(R.id.tvRightAndWrong)
        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)
        tv.text = "Correct answer is ${viewData.getCorrectAnswer()}\nYour answer is $userAnswer"
        btnContinue.setOnClickListener {
            mViewModel.nextQuestion()
            dialog.dismiss()
        }
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.show()

    }

    private fun onRightAnswer() {
        countRight++
        lifecycleScope.launch {
            val context = requireContext()
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_correct_answer)
            val displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels
            val dialogWidth = (screenWidth * 0.8).toInt()
            val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT


            dialog.window?.setLayout(dialogWidth, dialogHeight)
            dialog.window?.let {
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            dialog.show()
            delay(1500)
            dialog.dismiss()
            mViewModel.nextQuestion()
        }
    }

    private fun showCongratsDialog() {
        val context = requireContext()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_congrate)
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val dialogWidth = (screenWidth * 0.8).toInt()
        val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(dialogWidth, dialogHeight)
        val correct = dialog.findViewById<TextView>(R.id.tvCorrect)
        correct.text = "Correct answer: ${countRight}"
        val wrong = dialog.findViewById<TextView>(R.id.tvWrong)
        wrong.text = "Correct answer: ${countWrong}"
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelCongrats)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        startTime = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        duration += System.currentTimeMillis() - startTime
    }

    override fun onStop() {
        super.onStop()
        mainActivity.updateStatsInfo(mViewModel.setView.id, duration, countRight, countWrong)
    }
}