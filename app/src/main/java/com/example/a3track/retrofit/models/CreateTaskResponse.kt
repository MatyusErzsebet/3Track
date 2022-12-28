package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class CreateTaskResponse(
    @SerializedName("message")
    val message: String
)
