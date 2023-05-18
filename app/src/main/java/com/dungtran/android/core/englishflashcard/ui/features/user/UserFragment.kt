package com.dungtran.android.core.englishflashcard.ui.features.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentUserBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.features.home.HomeFragmentDirections
import com.dungtran.android.core.englishflashcard.ui.features.home.HomeViewModel
import com.dungtran.android.core.englishflashcard.ui.features.home.adapter.HomeAdapter
import com.dungtran.android.core.englishflashcard.ui.features.home.adapter.HomeItemClickListener
import com.dungtran.android.core.englishflashcard.ui.features.user.adapter.StatsAdapter
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : BaseScreenWithViewModelFragment<FragmentUserBinding>() {

    private val mViewModel: UserViewModel by viewModels()
    private val mAdapter by lazy {
        StatsAdapter(requireContext())
    }
    override fun getLayout(): Int {
        return R.layout.fragment_user
    }

    override fun initView() {
        binding.apply {
            viewModel = mViewModel
        }
        setRecycleView()
        observe()
    }

    override fun initViewModel() {

    }

    private fun setRecycleView() {
        binding.apply {
            rcStats.adapter = mAdapter
            rcStats.setHasFixedSize(true)
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