package com.dungtran.android.core.englishflashcard.ui.first.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.FragmentLoginBinding
import com.dungtran.android.core.englishflashcard.ui.MainActivity
import com.dungtran.android.core.englishflashcard.ui.core.BaseFragment
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenFragment
import com.dungtran.android.core.englishflashcard.ui.first.FirstActivity
import com.dungtran.android.core.englishflashcard.ui.first.register.RegisterFragmentDirections
import com.dungtran.android.core.englishflashcard.utils.StringUtils
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private lateinit var auth: FirebaseAuth

    override fun getLayout(): Int {
        return  R.layout.fragment_login
    }

    override fun initView() {
        binding.apply {
            btnLogin.setOnClickListener {
                performSignIn()
            }

            tvSignup.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }

            tvForgotPassword.setOnClickListener {
                createResetDialog()
            }
        }
    }

    override fun performBeforeInitView() {
        auth = Firebase.auth

    }

    private fun createResetDialog() {
        val email = binding.edtEmail.text.toString()
        if (email.isEmpty()) {
            ToastUtils.showToast(requireContext(), "Please fill out email!")
            return
        }

        if (!StringUtils.validateEmail(email)) {
            ToastUtils.showToast(requireContext(), "Wrong email format!")
            return
        }
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Reset password!")
        alertDialogBuilder.setMessage("Are you sure you want to resetPassword?\nAn email will send to your email.")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            forgotPassword()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun performSignIn() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            ToastUtils.showToast(requireContext(), "Please fill all fields!")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    requireActivity().startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                } else {

                    ToastUtils.showToast(requireContext(), "Authentication failed.")
                }
            }
            .addOnFailureListener {
                ToastUtils.showToast(requireContext(), "Error occurred ${it.localizedMessage}")
            }
    }

    private fun forgotPassword() {
        val email = binding.edtEmail.text.toString()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    ToastUtils.showToast(requireContext(), "Email reset password had been sent to $email")
                }
            }
            .addOnFailureListener {
                ToastUtils.showToast(requireContext(), "Error occurred ${it.localizedMessage}")
            }
    }

}