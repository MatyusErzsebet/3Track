package com.example.a3track.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

class Constants {
    companion object{
        val TITLE = "title"
        val SHAREDPREF = "sharedPref"
        val EMAIL = "email"
        val TOKEN = "token"
        val USERID = "userId"
        val DEPARTMENT = "department"
        val ASSIGNER = "assigner"
        val ASSIGNEE = "assignee"
        val ASSIGNDATE = "assignDate"
        val PRIORITY = "priority"
        val PROGRESS = "progress"
        val DESCRIPTION = "description"
        val DEADLINE = "deadline"
    }

}

class Utils {
    companion object {
        fun getDateString(time: Long): String {
            val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH)
            return simpleDateFormat.format(time)
        }

        fun getPriorityNameFromNumber(priority: Int): String{
            return when(priority){
                0 -> "Low"
                1 -> "Medium"
                2 -> "High"
                else -> "High"
            }
        }
    }
}