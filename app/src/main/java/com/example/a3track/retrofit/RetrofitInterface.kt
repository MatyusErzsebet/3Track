package com.example.a3track.retrofit


import com.example.a3track.retrofit.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {
    @POST("login")
    suspend fun login(@Body loginModel: LoginModel): Response<LoginResponse>

    @POST("users/forgetPassword")
    suspend fun resetPassword(@Body forgetPasswordModel: ForgetPasswordModel): Response<ForgetPasswordResponse>

    @GET("task/getTasks")
    suspend fun getTasks(@Header ("token") token : String): Response<List<GetTasksModel>>

    @GET("department")
    suspend fun getDepartments(@Header ("token") token : String): Response<List<GetDepartmentsModel>>

    @GET("users")
    suspend fun getUsers(@Header ("token") token : String): Response<List<GetUsersModel>>

    @POST("user/refreshToken")
    suspend fun refreshToken(@Body refreshTokenModel: RefreshTokenModel): Response<RefreshTokenResponse>

    @POST("task/create")
    suspend fun createTask(@Header("token") token: String, @Body createTaskModel: CreateTaskModel): Response<CreateTaskResponse>
}