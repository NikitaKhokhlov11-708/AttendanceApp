package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView

interface TeacherAttendanceView : MvpView {
    fun getCurrentUser()
    fun showSubj()
    fun showCalendar()
    fun showStudentsDropDown()
    fun showStat()
    fun getSelectedCourse()
    fun showGroupsDropdown()
    fun getSelectedGroup()
    fun getSelectedStudent()
}