package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("token")
    val token: String
)
