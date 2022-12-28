package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("passwordKey")
    private val passwordKey: String,
    @SerializedName("email")
    private val email: String
)
