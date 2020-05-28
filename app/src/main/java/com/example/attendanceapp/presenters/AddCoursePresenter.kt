package com.example.attendanceapp.presenters

import androidx.annotation.NonNull
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.Course
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.AddCourseView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@InjectViewState
class AddCoursePresenter : MvpPresenter<AddCourseView>() {

    lateinit var cUser: User
    val db = Database()
    var selectedItems: List<String> = mutableListOf()
    var courseFlag: String = ""

    fun getUser(user: User) {
        cUser = user
    }

    fun addCourse(name: String) {
        val course = Course()
        course.name = name
        course.courseId = db.database.getReference("courses").push().key
        course.teacherId = cUser.userId
        course.teacherName = cUser.lastname + " " +
                cUser.firstname + " " +
                cUser.middlename
        course.type = courseFlag
        course.itemsList = selectedItems

        db.addCourse(course)
        viewState.changeFragment()
    }

    fun getAllCourses() {
        courseFlag = "courses"
        viewState.clearSpinner()
        db.database.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (valueRes in dataSnapshot.children) {
                    if (valueRes.child("isStudent").value == true)
                        viewState.addItemToSpinner(valueRes.child("course").value.toString())
                }
            }
        })
    }

    fun getAllGroups() {
        courseFlag = "groups"
        viewState.clearSpinner()
        db.database.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (valueRes in dataSnapshot.children) {
                    if (valueRes.child("isStudent").value == true)
                        viewState.addItemToSpinner(valueRes.child("group").value.toString())
                }
            }
        })
    }

    fun getAllStudents() {
        viewState.clearSpinner()
        courseFlag = "students"
        db.database.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (valueRes in dataSnapshot.children) {
                    if (valueRes.child("isStudent").value == true)
                        viewState.addItemToSpinner(
                            valueRes.child("lastname").value.toString() + " " +
                                    valueRes.child("firstname").value.toString() + " " +
                                    valueRes.child("middlename").value.toString() + " " +
                                    valueRes.child("group").value.toString()
                        )
                }
            }
        })
    }
}