package com.dungtran.android.core.englishflashcard.ui.features.discovery

import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentDiscoveryBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.features.discovery.adapter.DiscoveryAdapter
import com.dungtran.android.core.englishflashcard.ui.features.discovery.adapter.SetItemClickListener
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoveryFragment : BaseScreenWithViewModelFragment<FragmentDiscoveryBinding>() {
    private val mViewModel: DiscoveryViewModel by viewModels()

    private val mAdapter by lazy {
        DiscoveryAdapter().apply {
            listener = object : SetItemClickListener {
                override fun onItemClick(item: SetView, position: Int) {
                    findNavController().navigate(
                        DiscoveryFragmentDirections.actionDiscoveryFragmentToSetFragment(item).setIsRemote(1)
                    )
                }

                override fun onItemLongClick(item: SetView, position: Int): Boolean {
                    showDownloadDialog(item)
                    return true
                }
            }
        }
    }
    override fun getLayout(): Int {
        return R.layout.fragment_discovery
    }

    override fun initView() {
        setRecycleView()
        observe()

        binding.apply {
            viewModel = mViewModel
        }
    }

    override fun initViewModel() {

    }

    private fun setRecycleView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.apply {
            rcSet.adapter = mAdapter
            rcSet.setHasFixedSize(true)
            rcSet.layoutManager = layoutManager
        }

        mAdapter.addLoadStateListener { loadState ->
            binding.noInternetLayout.root.visibility = View.GONE
            if (loadState.refresh is LoadState.Loading){
                binding.loadingLayout.root.visibility = View.VISIBLE
            }
            else{
                binding.loadingLayout.root.visibility = View.GONE
                if (loadState.append is LoadState.Loading) {
                    binding.loadMoreLayout.visibility = View.VISIBLE
                } else {
                    binding.loadMoreLayout.visibility = View.GONE
                }

                if (loadState.append is LoadState.Error) {
                    binding.noInternetLayout.root.visibility = View.VISIBLE
                }

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    binding.noInternetLayout.root.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
//            mViewModel.myData.collect {
//                when (it.loadingStatus) {
//                    LoadingStatus.Success -> {
//                        val body = (it as DataResponse.DataSuccess).body
//                        mAdapter.submit(body)
//                    }
//                    else -> {
//
//                    }
//                }
//            }

            mViewModel.movies.collectLatest {
                mAdapter.submitData(it)
            }
        }

        mViewModel.savingStatus.observe(this) {
            when(it) {
                LoadingStatus.Success -> {
                    ToastUtils.showToast(requireContext(), "Add set success")
                }
                LoadingStatus.Error -> {
                    ToastUtils.showToast(requireContext(), "Add set fail")
                }
                else ->{}
            }
        }
    }

    private fun showDownloadDialog(setView: SetView) {
        AlertDialog.Builder(context)
            .setTitle("Confirm")
            .setMessage("Are you sure you want to download \'${setView.title}\'?")
            .setPositiveButton("Download") { _, _ ->
                mViewModel.saveSet(setView)
            }
            .setNegativeButton("Cancel") { dialog, _ ->

                dialog.dismiss()
            }
            .create()
            .show()
    }

}