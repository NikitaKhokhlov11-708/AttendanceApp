package com.example.attendanceapp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.TeacherView

@InjectViewState
class TeacherPresenter : MvpPresenter<TeacherView>() {
    lateinit var cUser: User
    val database = Database()

    fun getUser(user: User) {
        cUser = user
    }
}