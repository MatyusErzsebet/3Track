package com.example.a3track.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.ForgetPasswordModel
import com.example.a3track.retrofit.models.ForgetPasswordResponse
import com.example.a3track.retrofit.models.LoginModel
import com.example.a3track.retrofit.models.LoginResponse
import kotlinx.coroutines.*

class ResetPasswordViewModel: ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val resetResponse = MutableLiveData<ForgetPasswordResponse>()
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    private var inserted: Int? = null



    fun getResetResponse(email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = Proxy.resetPassword(ForgetPasswordModel(email = email))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    resetResponse.postValue(responseBody)
                    loading.value = false
                    inserted = resetResponse.value?.code
                } else {
                    onError("Error when resetting password!")
                }
            }
        }


    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun cancelJob(){
        job?.cancel()
    }
}