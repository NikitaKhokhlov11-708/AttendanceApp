package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView

interface CoursesView : MvpView {
    fun getCurrentUser()
    fun initRecycler()
}