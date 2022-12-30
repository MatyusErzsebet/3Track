package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class UpdateProfileModel(
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
