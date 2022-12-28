package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class GetDepartmentsModel(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("name")
    val name: String
)
