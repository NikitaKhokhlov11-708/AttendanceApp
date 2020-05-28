package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView

interface StatisticsView : MvpView {
    fun getCurrentUser()
    fun viewStat()
    fun changeTableName(title: String)
}