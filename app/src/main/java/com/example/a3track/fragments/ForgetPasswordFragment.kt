package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a3track.MainActivity
import com.example.a3track.R
import com.example.a3track.utils.Constants
import com.example.a3track.viewmodels.ResetPasswordViewModel
import com.google.android.material.textfield.TextInputLayout

class ForgetPasswordFragment : Fragment() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var resetViewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetViewModel =
            ViewModelProvider(requireActivity()).get(ResetPasswordViewModel::class.java)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forget_password, container, false)

        emailInputLayout = view.findViewById(R.id.emailInputLayout2)
        emailEditText = view.findViewById(R.id.emailEditText2)
        resetPasswordButton = view.findViewById(R.id.resetpwButton)



        setOnClickListenerForResetButton()

        return view
    }

    private fun setOnClickListenerForResetButton() {

        resetPasswordButton.setOnClickListener {
            observeResetResponse()
            var areErrors = false
            if (emailEditText.text == null) {
                emailInputLayout.error = "Email field is empty!"
                areErrors = true
            } else if (emailEditText.text.isEmpty()) {
                emailInputLayout.error = "Email field is empty!"
                areErrors = true

            } else
                emailInputLayout.error = null


            if (!areErrors) {
                resetViewModel.getResetResponse(emailEditText.text.toString())
            }
        }

    }

    @SuppressLint("CommitPrefEdits")
    private fun observeResetResponse() {

        resetViewModel.resetResponse.observe(viewLifecycleOwner) {
            if (it.code == 1) {
                Toast.makeText(requireContext(), "Password reset!", Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, LoginFragment())
                        .commit()
                }, 3000)
            }
            else
                Toast.makeText(requireContext(), "Password not reset!", Toast.LENGTH_SHORT).show()

        }


        resetViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        resetViewModel.cancelJob()
    }
}

