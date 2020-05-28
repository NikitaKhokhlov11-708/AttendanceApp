package com.example.attendanceapp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.Course
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.CourseDetailsView

@InjectViewState
class CourseDetailsPresenter : MvpPresenter<CourseDetailsView>() {

    lateinit var cUser: User
    lateinit var cCourse: Course
    val database = Database()
    lateinit var id: String

    fun getUser(user: User) {
        cUser = user
    }

    fun getCourse(course: Course) {
        cCourse = course
        viewState.fillData(cCourse)
    }

    fun delete() {
        database.deleteCourse(cCourse)
        viewState.changeFragment()
    }

    fun checkInStudent(mac: String) {
        database.checkinStudent(mac, cCourse, id)
    }

    fun addAttendance() {
        id = database.database.getReference("attendance").push().key!!
        database.addAttendance(id, cCourse)
    }
}