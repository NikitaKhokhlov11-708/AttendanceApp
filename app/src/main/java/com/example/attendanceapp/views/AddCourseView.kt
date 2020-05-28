package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView

interface AddCourseView : MvpView {
    fun getCurrentUser()
    fun addCourse()
    fun addItemToSpinner(item: String)
    fun clearSpinner()
    fun changeFragment()
}