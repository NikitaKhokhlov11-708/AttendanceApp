package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView
import com.example.attendanceapp.models.Course

interface CourseDetailsView : MvpView {
    fun getCurrentUser()
    fun deleteCourse()
    fun changeFragment()
    fun getCurrentCourse()
    fun fillData(course: Course)
    fun onCheckInClick()
}