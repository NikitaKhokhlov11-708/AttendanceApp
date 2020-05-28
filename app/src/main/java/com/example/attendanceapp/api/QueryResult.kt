package com.example.attendanceapp.api


data class Student(
    var successful: Boolean,
    var user_id: Int,
    var employee: Boolean,
    var student: Boolean,
    var student_info: StudentInfo,
    var person: Boolean,
    var firstname: String,
    var lastname: String,
    var middlename: String,
    var p2: String
)

data class StudentInfo(
    var student_id: Int,
    var student_email: String,
    var photo: String,
    var photo_id: Int,
    var sex: String,
    var student_birth_date: String,
    var student_birth_place: String,
    var student_group_id: Int,
    var student_specialization_name: String,
    var student_speciality_name: String,
    var student_institute_name: String,
    var student_current_course: Int,
    var student_time_education: Int,
    var student_group_name: String
)