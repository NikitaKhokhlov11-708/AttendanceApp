package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView

interface StudentAttendanceView : MvpView {
    fun getCurrentUser()
    fun showSubj()
    fun showCalendar()
    fun showStat()
}