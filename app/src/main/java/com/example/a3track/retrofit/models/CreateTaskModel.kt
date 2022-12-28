package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class CreateTaskModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("assignedToUserId")
    val assigneeId: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("deadline")
    val deadLine: String,
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("status")
    val status: Int
)
