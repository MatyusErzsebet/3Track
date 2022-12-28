package com.example.a3track

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.sip.SipErrorCode.TIME_OUT
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import com.example.a3track.fragments.SplashFragment
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.retrofit.models.RefreshTokenResponse
import com.example.a3track.utils.Constants
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {
    @SuppressLint("CommitPrefEdits")

    val errorMessage = MutableLiveData<String>()
    val refreshTokenResponse = MutableLiveData<RefreshTokenResponse>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    var token: String? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPref = getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        sharedPref.edit()
        //sharedPref.edit().remove(Constants.TOKEN).apply()
        if(sharedPref.getString(Constants.TOKEN,null) != null ){

            refreshTokenResponse.observe(this) {
                sharedPref.edit().remove(Constants.TOKEN).apply()
                sharedPref.edit().putString(Constants.TOKEN, token).apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            errorMessage.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }

            getRefreshTokenResponse(sharedPref.getInt(Constants.USERID, 0))
        }

        else {

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, SplashFragment())
                .commit()

        }
    }


    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    fun getRefreshTokenResponse(userId: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = Proxy.refreshToken(userId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    token = responseBody.token
                    refreshTokenResponse.postValue(responseBody)
                } else {
                    onError("Can not refresh token!")
                }
            }
        }

    }
}