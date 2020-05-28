package com.example.attendanceapp.models

class User(
    var userId: Int?,
    var firstname: String?,
    var middlename: String?,
    var lastname: String?,
    var isStudent: Boolean?,
    var isEmployee: Boolean?,
    var course: Int?,
    var group: String?,
    var imei: String,
    var btAddress: String
)