package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class ForgetPasswordResponse(
    @SerializedName("inserted")
    val code: Int
)
