package com.example.attendanceapp.presenters

import androidx.annotation.NonNull
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.Course
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.CoursesView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


@InjectViewState
class CoursesPresenter : MvpPresenter<CoursesView>() {

    lateinit var cUser: User
    val database = Database()
    var myList = ArrayList<Course>()

    fun getUser(user: User) {
        cUser = user
    }

    fun getCourses() {
        database.database.getReference("courses")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    myList.clear()
                    for (course in dataSnapshot.children) {
                        if (course.child("teacherId").value.toString().toInt() == cUser.userId) {
                            val list: MutableList<String> = mutableListOf()
                            for (item in course.child("itemsList").children) {
                                list.add(item.value.toString())
                                val myCourse = Course()
                                myCourse.name = course.child("name").value.toString()
                                myCourse.courseId = course.child("courseId").value.toString()
                                myCourse.teacherId =
                                    course.child("teacherId").value.toString().toInt()
                                myCourse.teacherName = course.child("teacherName").value.toString()
                                myCourse.type = course.child("type").value.toString()
                                myCourse.itemsList = list

                                myList.add(myCourse)
                            }
                        }
                    }
                    viewState.initRecycler()
                }
            })
    }
}