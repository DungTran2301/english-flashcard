package com.dungtran.android.core.englishflashcard.ui.core
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.FragmentPolicyAndTermBinding

class PolicyAndTermFragment : BaseScreenFragment<FragmentPolicyAndTermBinding>() {
    private val args: PolicyAndTermFragmentArgs by navArgs()

    override fun getLayout(): Int {
        return R.layout.fragment_policy_and_term
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (args.param == 1) {
            binding.toolbar.title = requireContext().resources.getString(R.string.term_of_service)
            binding.wvPolicy.loadUrl("file:///android_asset/term_of_service.html")
        }
        else {
            binding.toolbar.title = requireContext().resources.getString(R.string.privacy_policy)
            binding.wvPolicy.loadUrl("file:///android_asset/policy.html")
        }
    }

    override fun performBeforeInitView() {
        //
    }
}