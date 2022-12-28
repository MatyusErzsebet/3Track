package com.example.a3track.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.GetTasksModel
import com.example.a3track.utils.Constants
import kotlinx.coroutines.*

class TasksViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    val errorMessage = MutableLiveData<String>()
    val tasksResponse = MutableLiveData<List<GetTasksModel>>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    val taskList: MutableList<GetTasksModel> = mutableListOf()


    init{
        getTasks()
    }

    fun getTasks(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = Proxy.getTasks(context.getSharedPreferences(Constants.SHAREDPREF,Context.MODE_PRIVATE).getString(Constants.TOKEN,"")!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    tasksResponse.postValue(responseBody)
                    loading.value = false
                    taskList.addAll(responseBody)
                } else {
                    onError("Can not get tasks!")
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
}