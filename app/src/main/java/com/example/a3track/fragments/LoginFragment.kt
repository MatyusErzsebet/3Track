package com.example.a3track.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.a3track.MainActivity
import com.example.a3track.R
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.LoginModel
import com.example.a3track.retrofit.models.LoginResponse
import com.example.a3track.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*


class LoginFragment : Fragment() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var clickHereTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var sharedPref: SharedPreferences

    val errorMessage = MutableLiveData<String>()
    val loginResponse = MutableLiveData<LoginResponse>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    var userId: Int? = null
    var token: String? = null

    fun getLoginResponse(email: String, password: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = Proxy.login(LoginModel(passwordKey = password, email = email))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    loginResponse.postValue(responseBody)
                    loading.value = false
                    userId = responseBody.userId
                    token = responseBody.token

                } else {
                    onError("Invalid email or password")
                }
            }
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailInputLayout = view.findViewById(R.id.emailInputLayout)
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        clickHereTextView = view.findViewById(R.id.clickHereTextView)
        loginButton = view.findViewById(R.id.loginButton)

        observeLoginResponse()

        setOnClickListenerForLoginButton()


        /*
        clickHereTextView.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().
            replace(R.id.fragmentContainerView, ForgetPasswordFragment())
                .addToBackStack(null)
                .commit()
        }
        */


        return view
    }

    private fun setOnClickListenerForLoginButton() {

        loginButton.setOnClickListener {
            var areErrors = false
            if (emailEditText.text == null) {
                emailInputLayout.error = "Email field is empty!"
                areErrors = true
            } else if (emailEditText.text.isEmpty()) {
                emailInputLayout.error = "Email field is empty!"
                areErrors = true

            } else
                emailInputLayout.error = null

            if (passwordEditText.text == null) {
                passwordEditText.error = "Email field is empty!"
                areErrors = true
            } else if (passwordEditText.text.isEmpty()) {
                passwordInputLayout.error = "Email field is empty!"
                areErrors = true
            } else
                passwordInputLayout.error = null


            if (!areErrors) {

                getLoginResponse(email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString())

            }

        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeLoginResponse(){

        val edit = sharedPref.edit()

        loginResponse.observe(viewLifecycleOwner) {
            edit.putString(Constants.EMAIL, emailEditText.text.toString()).apply()
            edit.putInt(Constants.USERID, userId!!).apply()
            edit.putString(Constants.TOKEN, token!!).apply()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

    }
}