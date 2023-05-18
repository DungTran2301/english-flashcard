package com.dungtran.android.core.englishflashcard.ui.features.study.card

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.response.DataResponse
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentStudyBinding
import com.dungtran.android.core.englishflashcard.databinding.ItemCardBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.core.platform.FlipAnimation
import com.dungtran.android.core.englishflashcard.ui.core.platform.LONG_ANIMATION
import com.dungtran.android.core.englishflashcard.ui.core.recyclerview.BaseViewHolder
import com.dungtran.android.core.englishflashcard.ui.features.setcards.SetViewModel
import com.dungtran.android.core.englishflashcard.ui.features.study.card.adapter.OnCardClickListener
import com.dungtran.android.core.englishflashcard.ui.features.study.card.adapter.StudyAdapter
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.utils.sound.MediaPlayerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class StudyFragment : BaseScreenWithViewModelFragment<FragmentStudyBinding>() {
    private val mViewModel: SetViewModel by viewModels()

    private val args: StudyFragmentArgs by navArgs()

    lateinit var mLayoutManager: LinearLayoutManager

    private var currentCardPosition = 0

    private val mediaPlayerUtils: MediaPlayerUtils = MediaPlayerUtils()

    private var isAutoPlayIsOn: Boolean = false

    private var job = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.Default + job)

    private var startTime: Long = 0
    private var duration: Long = 0


    private val mAdapter by lazy {
        StudyAdapter().apply {
            listener = object : OnCardClickListener {
                override fun onItemClick(item: CardView, position: Int) {

                }

                override fun onSpeakerClick(item: CardView) {
                    mediaPlayerUtils.playSound(item.vocalization)
                }

            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.fragment_study
    }

    override fun initView() {
        binding.apply {
            viewModel = mViewModel
            snap = LinearSnapHelper()
            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            toolBar.title = "1 / ${mViewModel.setView.count}"
            imbAutoPay.setOnClickListener {
                setAutoState(isAutoPlayIsOn)
            }
        }
        setRecycleView()
        observe()
    }

    override fun initViewModel() {
        mViewModel.setSet(args.set, args.isRemote)
        mViewModel.getData()
    }


    private fun setRecycleView() {
        mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mLayoutManager.isSmoothScrollbarEnabled = true
        binding.rcCards.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        showCurrentCardPosition()
                    }
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

    private fun startAutoPlay() {
        job.cancel()
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.Default + job)
        scope.launch {
            mAdapter.list.forEachIndexed { index, card ->
                binding.rcCards.smoothScrollToPosition(index)
                mediaPlayerUtils.playSound(card.vocalization)
                delay(2000)
                val viewHolder = binding.rcCards.findViewHolderForLayoutPosition(index)
                if (viewHolder is BaseViewHolder) {
                    (viewHolder.binding as ItemCardBinding).apply {
                        val anim = FlipAnimation(front, back, LONG_ANIMATION.toInt())

                        root.startAnimation(anim)
                        delay(1500)

                        anim.reverse()
                        root.startAnimation(anim)
                    }
                }

            }
            job.cancel()
            setAutoState(currentStatus = true)
        }
    }

    private fun setAutoState(currentStatus: Boolean) {
        if (currentStatus) {
            binding.tvAutoPlay.visibility = View.INVISIBLE
            isAutoPlayIsOn = false

        } else {
            binding.tvAutoPlay.visibility = View.VISIBLE
            isAutoPlayIsOn = true
            startAutoPlay()
        }
    }

    fun cancelTask() {
        job.cancel()
    }

    private fun showCurrentCardPosition() {
        val pos = mLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (pos != -1) currentCardPosition = pos

        binding.toolBar.title =
            "${currentCardPosition + 1} / ${mAdapter.list.size}"

        if (mAdapter.list.isNotEmpty()) {
            binding.progressbar.progress = ((currentCardPosition + 1) * 100 / mAdapter.list.size)
        }
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
        mainActivity.updateStatsInfo(mViewModel.setView.id, duration)
    }
    override fun onDetach() {
        job.cancel()
        super.onDetach()

    }

}