package com.example.a3track.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a3track.retrofit.Proxy
import com.example.a3track.retrofit.models.GetUsersModel
import com.example.a3track.utils.Constants
import kotlinx.coroutines.*

class UsersViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext
    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val success = MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    lateinit var myUser : GetUsersModel
    lateinit var myMentor: GetUsersModel

    var userList: MutableList<GetUsersModel> = mutableListOf()

    init{
        getUsers()
    }

    fun getUsers(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = Proxy.getUsers(context.getSharedPreferences(Constants.SHAREDPREF,
                Context.MODE_PRIVATE).getString(Constants.TOKEN,"")!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    loading.value = false
                    userList.addAll(responseBody)
                    success.postValue(true)

                    myUser = userList.first {
                        it.id == context.getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE).getInt(Constants.USERID, 0)
                    }

                    myMentor = userList.first {
                        it.departmentId == myUser.departmentId && it.type == 0
                    }



                } else {
                    onError("Can not get users!")
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

    fun getUserNameById(id: Int) : String{
        val user =userList.find { it.id == id }!!
        return "${user.firstName} ${user.lastName}"
    }

}