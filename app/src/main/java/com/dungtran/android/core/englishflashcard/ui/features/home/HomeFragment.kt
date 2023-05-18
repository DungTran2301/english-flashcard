package com.dungtran.android.core.englishflashcard.ui.features.home

import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentHomeBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.features.home.adapter.HomeAdapter
import com.dungtran.android.core.englishflashcard.ui.features.home.adapter.HomeItemClickListener
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseScreenWithViewModelFragment<FragmentHomeBinding>() {

    private val mViewModel: HomeViewModel by viewModels()

    private val mAdapter by lazy {
        HomeAdapter().apply {
            listener = object : HomeItemClickListener {
                override fun onItemClick(item: SetView, position: Int) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSetFragment(item)
                    )
                }
            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initViewModel() {

    }

    override fun initView() {
        setRecycleView()
        observe()

        binding.apply {
            viewModel = mViewModel
            fab.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToCreateSetFragment()
                )
            }
        }
    }

    private fun setRecycleView() {
        binding.apply {
            rcSet.adapter = mAdapter
            rcSet.setHasFixedSize(true)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedSet: SetView = mAdapter.getCurrentItem(viewHolder.bindingAdapterPosition)

                val position = viewHolder.bindingAdapterPosition

                mAdapter.remove(position = viewHolder.bindingAdapterPosition)

                mAdapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                AlertDialog.Builder(context)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to delete \'${deletedSet.title}\'?")
                    .setPositiveButton("OK") { _, _ ->
                        mViewModel.removeSet(deletedSet)
                    }
                    .setNegativeButton("Undo") { dialog, _ ->
                        mAdapter.add(position, deletedSet)
                        mAdapter.notifyItemInserted(position)
                        dialog.dismiss()
                    }
                    .create()
                    .show()

            }
        }).attachToRecyclerView(binding.rcSet)
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