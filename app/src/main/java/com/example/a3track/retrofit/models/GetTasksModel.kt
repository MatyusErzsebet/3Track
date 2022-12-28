package com.example.a3track.retrofit.models

import com.google.gson.annotations.SerializedName

data class GetTasksModel(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("created_by_user_ID")
    val assignerId: Int,
    @SerializedName("asigned_to_user_ID")
    val assigneeId: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("deadline")
    val deadline: Long,
    @SerializedName("department_ID")
    val departmentId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("progress")
    val progress: Int?,
    var departmentName: String,
    var assignerName: String,
    @SerializedName("created_time")
    val assignDate: Long,
    var assigneeName: String

)
