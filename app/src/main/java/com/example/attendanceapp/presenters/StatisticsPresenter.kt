package com.example.attendanceapp.presenters

import androidx.annotation.NonNull
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.StatisticsView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@InjectViewState
class StatisticsPresenter : MvpPresenter<StatisticsView>() {

    lateinit var cUser: User
    val database = Database()
    val list = mutableListOf<Array<String>>()
    var courseName = ""
    var group = ""
    var student = ""
    var dateT = ""

    fun getUser(user: User) {
        cUser = user
    }

    fun getAllStatistics() {
        list.clear()
        FirebaseDatabase.getInstance().getReference("attendance")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("name").value.toString() == courseName &&
                            c.child("date").value.toString() == dateT
                        ) {
                            for (stud in c.child("students").children)
                                list.add(arrayOf(stud.value.toString()))
                        }

                    }
                    val dateParsed = dateT.split(" ")
                    viewState.changeTableName(
                        "Все за " + dateParsed[0] + "." +
                                dateParsed[1] + "." +
                                dateParsed[2]
                    )
                    viewState.viewStat()
                }
            })
    }

    fun getGroupStatistics() {
        list.clear()
        FirebaseDatabase.getInstance().getReference("attendance")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("name").value.toString() == courseName &&
                            c.child("date").value.toString() == dateT
                        ) {
                            for (stud in c.child("students").children) {
                                if (stud.value.toString().split(" ")[3] == group)
                                    list.add(arrayOf(stud.value.toString()))
                            }
                        }
                    }
                    val dateParsed = dateT.split(" ")
                    viewState.changeTableName(
                        group + " за " + dateParsed[0] + "." +
                                dateParsed[1] + "." +
                                dateParsed[2]
                    )
                    viewState.viewStat()
                }
            })
    }

    fun getStudentStatistics() {
        list.clear()
        FirebaseDatabase.getInstance().getReference("attendance")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("name").value.toString() == courseName) {
                            var flag = "-"
                            for (stud in c.child("students").children) {
                                if (stud.value.toString() == student)
                                    flag = "+"
                            }
                            list.add(arrayOf(c.child("date").value.toString(), flag))
                        }
                    }
                    viewState.changeTableName(
                        student
                    )
                    viewState.viewStat()
                }
            })
    }

    fun getStatistics(courseId: String) {
        list.clear()
        FirebaseDatabase.getInstance().getReference("attendance")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("courseId").value.toString() == courseId) {
                            when (c.child("type").value.toString()) {
                                "courses" -> {
                                    for (item in c.child("itemsList").children) {
                                        if (item.value.toString() == cUser.course.toString()) {
                                            var flag = "-"
                                            if (c.child("students").hasChild(cUser.userId.toString()))
                                                flag = "+"
                                            list.add(
                                                arrayOf(
                                                    c.child("date").value.toString(),
                                                    flag
                                                )
                                            )
                                        }
                                    }
                                }
                                "groups" -> {
                                    for (item in c.child("itemsList").children) {
                                        if (item.value.toString() == cUser.group.toString()) {
                                            var flag = "-"
                                            if (c.child("students").hasChild(cUser.userId.toString()))
                                                flag = "+"
                                            list.add(
                                                arrayOf(
                                                    c.child("date").value.toString(),
                                                    flag
                                                )
                                            )
                                        }
                                    }
                                }
                                "students" -> {
                                    for (item in c.child("itemsList").children) {
                                        if (item.value.toString() == cUser.lastname + " " +
                                            cUser.firstname + " " +
                                            cUser.middlename + " " +
                                            cUser.group
                                        ) {
                                            var flag = "-"
                                            if (c.child("students").hasChild(cUser.userId.toString()))
                                                flag = "+"
                                            list.add(
                                                arrayOf(
                                                    c.child("date").value.toString(),
                                                    flag
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            viewState.changeTableName(
                                c.child(
                                    "name"
                                ).value.toString()
                            )
                        }
                    }
                    viewState.viewStat()
                }
            })

    }

    fun getStatisticsByDate(date: String) {
        list.clear()
        FirebaseDatabase.getInstance().getReference("attendance")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("date").value.toString() == date) {
                            when (c.child("type").value.toString()) {
                                "courses" -> {
                                    for (item in c.child("itemsList").children) {
                                        if (item.value.toString() == cUser.course.toString()) {
                                            var flag = "-"
                                            if (c.child("students").hasChild(cUser.userId.toString()))
                                                flag = "+"
                                            list.add(
                                                arrayOf(
                                                    c.child("name").value.toString(),
                                                    flag
                                                )
                                            )
                                        }
                                    }
                                }
                                "groups" -> {
                                    for (item in c.child("itemsList").children) {
                                        if (item.value.toString() == cUser.group.toString()) {
                                            var flag = "-"
                                            if (c.child("students").hasChild(cUser.userId.toString()))
                                                flag = "+"
                                            list.add(
                                                arrayOf(
                                                    c.child("name").value.toString(),
                                                    flag
                                                )
                                            )
                                        }
                                    }
                                }
                                "students" -> {
                                    for (item in c.child("itemsList").children) {
                                        if (item.value.toString() == cUser.lastname + " " +
                                            cUser.firstname + " " +
                                            cUser.middlename + " " +
                                            cUser.group
                                        ) {
                                            var flag = "-"
                                            if (c.child("students").hasChild(cUser.userId.toString()))
                                                flag = "+"
                                            list.add(
                                                arrayOf(
                                                    c.child("name").value.toString(),
                                                    flag
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    val dateParsed = date.split(" ")
                    viewState.changeTableName(
                        dateParsed[0] + "." +
                                dateParsed[1] + "." +
                                dateParsed[2]
                    )
                    viewState.viewStat()
                }
            })

    }
}