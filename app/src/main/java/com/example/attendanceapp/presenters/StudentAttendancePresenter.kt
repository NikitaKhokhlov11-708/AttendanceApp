package com.example.attendanceapp.presenters

import androidx.annotation.NonNull
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.StudentAttendanceView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@InjectViewState
class StudentAttendancePresenter : MvpPresenter<StudentAttendanceView>() {

    lateinit var cUser: User
    val database = Database()
    val list = ArrayList<String>()
    var courseId: String = ""
    var date: String = ""

    fun getUser(user: User) {
        cUser = user
    }

    fun getAvaliableSubjects() {
        list.clear()
        FirebaseDatabase.getInstance().getReference("courses")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        when (c.child("type").value.toString()) {
                            "courses" -> {
                                for (course in c.child("itemsList").children) {
                                    if (course.value.toString() == cUser.course.toString())
                                        list.add(c.child("name").value.toString())
                                }
                            }
                            "groups" -> {
                                for (course in c.child("itemsList").children) {
                                    if (course.value.toString() == cUser.group.toString())
                                        list.add(c.child("name").value.toString())
                                }
                            }
                            "students" -> {
                                for (course in c.child("itemsList").children) {
                                    if (course.value.toString() == cUser.lastname + " " +
                                        cUser.firstname + " " +
                                        cUser.middlename + " " +
                                        cUser.group
                                    )
                                        list.add(c.child("name").value.toString())
                                }
                            }
                        }
                    }
                    viewState.showSubj()
                }
            })

    }

    fun getCourseByName(course: String) {
        FirebaseDatabase.getInstance().getReference("courses")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("name").value.toString() == course)
                            courseId = c.child("courseId").value.toString()
                    }
                    viewState.showStat()
                }
            })

    }
}