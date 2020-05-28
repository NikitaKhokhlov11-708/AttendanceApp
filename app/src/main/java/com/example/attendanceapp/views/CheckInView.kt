package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView

interface CheckInView : MvpView {
    fun getCurrentUser()
    fun checkIn()
    fun sendToast()
}