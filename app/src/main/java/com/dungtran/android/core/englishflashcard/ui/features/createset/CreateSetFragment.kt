package com.dungtran.android.core.englishflashcard.ui.features.createset

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.response.LoadingStatus
import com.dungtran.android.core.englishflashcard.databinding.FragmentCreateSetBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenWithViewModelFragment
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.AddPhaseDialogFragment
import com.dungtran.android.core.englishflashcard.ui.features.createset.addphase.AddPhaseDialogListener
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeAdapter
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeItem
import com.dungtran.android.core.englishflashcard.ui.features.createset.typedialog.TypeItemClickListener
import com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter.OnPhaseClickListener
import com.dungtran.android.core.englishflashcard.ui.features.setcards.adapter.PhaseAdapter
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import com.dungtran.android.core.englishflashcard.utils.sound.MediaPlayerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateSetFragment : BaseScreenWithViewModelFragment<FragmentCreateSetBinding>() {

    private val mViewModel: CreateSetViewModel by viewModels()

    private var isSave: Boolean = false

    private var mediaPlayerUtils = MediaPlayerUtils()

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
        return R.layout.fragment_create_set
    }

    override fun initView() {
        binding.apply {
            viewModel = mViewModel
            btnAddPhase.setOnClickListener {
                AddPhaseDialogFragment(object : AddPhaseDialogListener {
                    override fun onChoosePhase(cardView: CardView) {
                        mViewModel.addCard(cardView)

                    }

                }).show(childFragmentManager, "add")
            }

            toolBar.setNavigationOnClickListener {
                if (isSave) {
                    findNavController().navigateUp()
                }
                else {
                    showConfirmUnSaveDialog()
                }
            }

            tvType.setOnClickListener {
                showChooseTypeDialog()
            }

            btnSave.setOnClickListener {
                isSave = true
                val title = edtName.text.toString().trim()
                val type = tvType.text.toString().trim()
                if (title.isBlank() || type.isBlank()) {
                    ToastUtils.showToast(requireContext(), "Empty field")
                }
                else {
                    val setDB = SetDB()
                    setDB.title = title
                    setDB.type = type
                    mViewModel.save(setDB)
                }
            }
        }
        setupRecycleView()
        observe()
    }

    private fun setupRecycleView() {
        binding.apply {
            rcPhases.adapter = mAdapter
            rcPhases.setHasFixedSize(true)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            mViewModel.mListCard.collect {
                mAdapter.submit(it)
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

    override fun initViewModel() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showConfirmUnSaveDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm")
        builder.setMessage("This set will be unsave?")
        builder.setPositiveButton("OK") { _, _ ->
            findNavController().navigateUp()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showChooseTypeDialog() {
        val context = requireContext()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choose_type)
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val dialogWidth = (screenWidth * 0.8).toInt()
        val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(dialogWidth, dialogHeight)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.dialog_recycler_view)
        recyclerView.adapter =  TypeAdapter().apply {
            listener = object : TypeItemClickListener {
                override fun onItemClick(item: TypeItem, position: Int) {
                    binding.tvType.text = item.text
                    dialog.dismiss()
                }
            }
            submit(items)
        }

        val cancelButton = dialog.findViewById<Button>(R.id.dialog_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        private val items = listOf(
            TypeItem("Toeic", R.drawable.banner_education),
            TypeItem("Work", R.drawable.banner_work),
            TypeItem("Culture", R.drawable.banner_food_drink),
            TypeItem("Music and film", R.drawable.banner_music),
            TypeItem("Relationship", R.drawable.banner_relationship),
            TypeItem("Travel", R.drawable.banner_travel),
            TypeItem("Other", R.drawable.banner_other)
        )
    }

}