package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class ForgetPasswordModel(
    @SerializedName("email")
    private val email: String

)
