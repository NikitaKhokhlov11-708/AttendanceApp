package com.example.attendanceapp.presenters

import androidx.annotation.NonNull
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.TeacherAttendanceView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@InjectViewState
class TeacherAttendancePresenter : MvpPresenter<TeacherAttendanceView>() {

    lateinit var cUser: User
    val database = Database()
    val listSubj = ArrayList<String>()
    val listGroups = ArrayList<String>()
    val listStudents = ArrayList<String>()
    var courseName: String = ""
    var selectedGroup: String = ""
    var selectedStudent: String = ""
    var date: String = ""

    fun getUser(user: User) {
        cUser = user
    }

    fun getAvaliableSubjects() {
        listSubj.clear()
        FirebaseDatabase.getInstance().getReference("courses")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("teacherId").value.toString() == cUser.userId.toString())
                            listSubj.add(c.child("name").value.toString())
                    }
                    viewState.showSubj()
                }
            })
    }

    fun getGroups() {
        viewState.getSelectedCourse()
        listGroups.clear()
        FirebaseDatabase.getInstance().getReference("courses")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("name").value.toString() == courseName && c.child("type").value.toString() == "students")
                            for (stud in c.child("itemsList").children) {
                                val splitted = stud.value.toString().split(" ")
                                if (!listGroups.contains(splitted[3]))
                                    listGroups.add(splitted[3])
                            }
                    }

                    viewState.showGroupsDropdown()
                }
            })
    }

    fun getStudents() {
        viewState.getSelectedCourse()
        listStudents.clear()
        FirebaseDatabase.getInstance().getReference("courses")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (c in dataSnapshot.children) {
                        if (c.child("name").value.toString() == courseName && c.child("type").value.toString() == "students")
                            for (stud in c.child("itemsList").children) {
                                listStudents.add(stud.value.toString())
                            }
                    }

                    viewState.showStudentsDropDown()
                }
            })
    }
}