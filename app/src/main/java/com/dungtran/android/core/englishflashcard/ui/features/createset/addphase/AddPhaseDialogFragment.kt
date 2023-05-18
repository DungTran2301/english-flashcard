package com.dungtran.android.core.englishflashcard.ui.features.createset.addphase

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentAddPhaseDialogBinding
import com.dungtran.android.core.englishflashcard.ui.core.dialog.BaseDialogFragment
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.adapter.OnSearchResultClickListener
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.adapter.SearchResultAdapter
import com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter.OnPhaseClickListener
import com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter.PhaseAdapter
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import com.dungtran.android.core.englishflashcard.utils.sound.MediaPlayerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPhaseDialogFragment(listener: AddPhaseDialogListener)
    : BaseDialogFragment<FragmentAddPhaseDialogBinding>(listener) {

    private val mViewModel: AddPhaseViewModel by viewModels()

    private var mediaPlayerUtils = MediaPlayerUtils()

    private val mAdapter by lazy {
        SearchResultAdapter().apply {
            this.listener = object : OnSearchResultClickListener{
                override fun onItemClick(item: CardView, position: Int) {
                    listener.onChoosePhase(item)
                    dialog?.dismiss()
                }

                override fun onSpeakerClick(item: CardView) {
                    mediaPlayerUtils.playSound(item.vocalization)
                }

            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_add_phase_dialog
    }

    override fun initViews(view: View) {
        binding.apply {
            viewModel = mViewModel
            toolBar.setNavigationOnClickListener {
                dialog?.dismiss()
            }
        }
        setupSearchView()
        setupRecycleView()
        observe()

    }

    private fun setupRecycleView() {
        binding.apply {
            rcPhases.adapter = mAdapter
            rcPhases.setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null && query.isNotEmpty()) {
                        mViewModel.getWordInfo(query.trim())
                    }
                    return true
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    if (text != null && text.isNotEmpty()) {
//                        mViewModel.getWordInfo(text)
                    }
                    return true
                }
            })
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
    }
}