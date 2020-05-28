package com.example.attendanceapp.models

import java.io.Serializable

class Course : Serializable {
    var name: String? = null
    var courseId: String? = null
    var teacherId: Int? = null
    var teacherName: String? = null
    var type: String? = null
    var itemsList: List<String>? = null
}