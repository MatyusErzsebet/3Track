package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class GetUsersModel(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("department_id")
    val departmentId: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("image")
    val image: String,
    var departmentName: String
)
