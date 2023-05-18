package com.dungtran.android.core.englishflashcard.ui.features.setcards

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentSetBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.AddPhaseDialogFragment
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.AddPhaseDialogListener
import com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter.OnPhaseClickListener
import com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter.PhaseAdapter
import com.dungtran.android.core.englishflashcard.ui.features.setcards.bottomsheet.BottomSheetDialog
import com.dungtran.android.core.englishflashcard.ui.features.setcards.bottomsheet.OnBottomSheetClickListener
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import com.dungtran.android.core.englishflashcard.utils.sound.MediaPlayerUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetFragment : BaseScreenWithViewModelFragment<FragmentSetBinding>() {
    private val mViewModel: SetViewModel by viewModels()


    private val args: SetFragmentArgs by navArgs()
    private var mediaPlayerUtils= MediaPlayerUtils()

    private val mAdapter by lazy {
        PhaseAdapter().apply {
            listener = object : OnPhaseClickListener {
                override fun onItemClick(item: CardView, position: Int) {

                }

                override fun onSpeakerClick(item: CardView) {
                    mediaPlayerUtils.playSound(item.vocalization)
                }
            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.fragment_set
    }

    override fun initView() {
        binding.apply {
            viewModel = mViewModel

            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnLearn.setOnClickListener {
                createBottomDialog()
            }

            btnAdd.visibility = if (args.isRemote == 1) View.INVISIBLE else View.VISIBLE

            btnAdd.setOnClickListener {
                AddPhaseDialogFragment(object : AddPhaseDialogListener {
                    override fun onChoosePhase(cardView: CardView) {
                        mViewModel.addCard(cardView)
                    }

                }).show(childFragmentManager, "add")
            }
        }
        setRecycleView()
        observe()
    }


    override fun initViewModel() {
        mViewModel.setSet(args.set, args.isRemote)
        mViewModel.getData()
    }

    private fun createBottomDialog() {
        val bottomSheetDialogFragment = BottomSheetDialog(object : OnBottomSheetClickListener{
            override fun onItemClick(position: Int) {
                when(position) {
                    1 -> {
                        findNavController().navigate(SetFragmentDirections.actionSetFragmentToStudyFragment(args.set).setIsRemote(args.isRemote))
                    }
                    2 -> {
                        findNavController().navigate(SetFragmentDirections.actionSetFragmentToMultipleChoiceFragment(args.set).setIsRemote(args.isRemote))
                    }
                }

            }

        })

        bottomSheetDialogFragment.show(childFragmentManager, "bottom_sheet_dialog_fragment")
    }

    private fun setRecycleView() {
        binding.apply {
            rcPhases.adapter = mAdapter
            rcPhases.setHasFixedSize(true)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            mViewModel.myData.collect {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        val body = (it as DataResponse.DataSuccess).body
                        mAdapter.submit(body)
                    }
                    else -> {

                    }
                }
            }
        }

        mViewModel.savingStatus.observe(this) {
            when(it) {
                LoadingStatus.Success -> {
                    ToastUtils.showToast(requireContext(), "Add phase success")
                }
                LoadingStatus.Error -> {
                    ToastUtils.showToast(requireContext(), "Add phase fail")
                }
                else ->{}
            }
        }
    }
}