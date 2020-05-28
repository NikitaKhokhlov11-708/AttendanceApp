package com.example.attendanceapp.views

import com.arellomobile.mvp.MvpView
import com.example.attendanceapp.models.User


interface LoginView : MvpView {
    fun login(type: String)
    fun getText()
    fun sendErrorToast()
    fun sendTestToast(data: String)
    fun getImei()
    fun getBtAddress()
    fun saveUser(user: User)
    fun showProgress()
    fun hideProgress()
}