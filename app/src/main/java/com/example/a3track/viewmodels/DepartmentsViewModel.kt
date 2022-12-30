package com.example.a3track.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.GetDepartmentsModel
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.utils.Constants
import kotlinx.coroutines.*

class DepartmentsViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext
    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val success = MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    val departmentList: MutableList<GetDepartmentsModel> = mutableListOf()

    init{
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = Proxy.getDepartments(context.getSharedPreferences(Constants.SHAREDPREF,
                Context.MODE_PRIVATE).getString(Constants.TOKEN,"")!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    loading.value = false
                    departmentList.addAll(responseBody)
                    success.postValue(true)
                } else {
                    onError("Can not get departments!")
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

    fun getDepartmentNameById(id: Int) : String{
        return departmentList.find { it.id == id }!!.name
    }
}