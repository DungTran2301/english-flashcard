package com.dungtran.android.core.englishflashcard.ui.first.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.FragmentRegisterBinding
import com.dungtran.android.core.englishflashcard.ui.MainActivity
import com.dungtran.android.core.englishflashcard.ui.core.BaseFragment
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenFragment
import com.dungtran.android.core.englishflashcard.ui.first.login.LoginFragmentDirections
import com.dungtran.android.core.englishflashcard.utils.StringUtils
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private lateinit var auth: FirebaseAuth

    override fun getLayout(): Int {
        return R.layout.fragment_register
    }

    override fun initView() {
        binding.apply {
            btnRegister.setOnClickListener {
                performSignUp()
            }

            tvSignIn.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        }

    }

    override fun performBeforeInitView() {
        auth = Firebase.auth
    }

    private fun performSignUp() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val passwordConfirm = binding.edtConfirmPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            ToastUtils.showToast(requireContext(), "Please fill all fields!")
            return
        }

        if (!StringUtils.validateEmail(email)) {
            ToastUtils.showToast(requireContext(), "Wrong email format!")
            return
        }

        if (!StringUtils.validatePassword(password)) {
            ToastUtils.showToast(requireContext(), "Password need more than 8 characters!")
            return
        }

        if (password != passwordConfirm) {
            ToastUtils.showToast(requireContext(), "Wrong confirm password!")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    ToastUtils.showToast(requireContext(), "Register success!")
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                } else {

                    ToastUtils.showToast(requireContext(), "Authentication failed.")
                }
            }
            .addOnFailureListener {
                ToastUtils.showToast(requireContext(), "Error occurred ${it.localizedMessage}")
            }
    }

}