package com.example.a3track.retrofit

import com.example.a3track.retrofit.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Proxy {


    private const val BASE_URL = "https://tracker-3track.a2hosted.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    val service: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }

    suspend fun login(loginModel: LoginModel) = service.login(loginModel)
    suspend fun resetPassword(resetModel: ForgetPasswordModel) = service.resetPassword(resetModel)
    suspend fun getTasks(token: String) = service.getTasks(token)
    suspend fun getDepartments(token: String) = service.getDepartments(token)
    suspend fun getUsers(token: String) = service.getUsers(token)
    suspend fun refreshToken(userId: Int) = service.refreshToken(RefreshTokenModel(userId = userId))
    suspend fun createTask(token: String, createTaskModel: CreateTaskModel) = service.createTask(token, createTaskModel)
    suspend fun updateProfile(token: String, updateProfileModel: UpdateProfileModel) = service.updateUser(token, updateProfileModel)

}
