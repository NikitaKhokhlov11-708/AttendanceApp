package com.example.attendanceapp.database

import androidx.annotation.NonNull
import com.example.attendanceapp.models.Course
import com.example.attendanceapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class Database {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun checkUser(userId: String, imei: String): User? {
        var user: User? = null

        database.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    if (checkImei(userId, imei))
                        user = getUser(userId)
                }
            }
        })

        return user
    }

    fun checkImei(userId: String, imei: String): Boolean {
        var flag = false
        database.getReference("users/" + userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                flag = dataSnapshot.child("imei").value.toString() == imei
            }
        })

        return flag
    }

    fun getUser(userId: String): User {
        var user: User? = null
        database.getReference("users/" + userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)
            }
        })

        return user!!
    }

    fun addUser(user: User) {
        val map = HashMap<String, Any>()
        map["userId"] = user.userId!!
        map["firstname"] = user.firstname!!
        map["middlename"] = user.middlename!!
        map["lastname"] = user.lastname!!
        map["isStudent"] = user.isStudent!!
        map["isEmployee"] = user.isEmployee!!
        map["imei"] = user.imei
        map["btAddress"] = user.btAddress
        map["course"] = user.course!!
        map["group"] = user.group!!

        database.getReference("users/" + user.userId).setValue(map)
    }

    fun addCourse(course: Course) {
        val map = HashMap<String, Any>()
        map["courseId"] = course.courseId!!
        map["teacherId"] = course.teacherId!!
        map["teacherName"] = course.teacherName!!
        map["name"] = course.name!!
        map["type"] = course.type!!
        map["itemsList"] = course.itemsList!!

        database.getReference("courses/" + course.courseId).setValue(map)
    }

    fun deleteCourse(course: Course) {
        database.getReference("courses/" + course.courseId).setValue(null)
    }

    fun addAttendance(id: String, course: Course) {
        database.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val cal = Calendar.getInstance()
                val dateOnly = SimpleDateFormat("dd MM yyyy")
                val map = HashMap<String, Any>()
                map["courseId"] = course.courseId!!
                map["date"] = dateOnly.format(cal.time)
                map["itemsList"] = course.itemsList!!
                map["name"] = course.name!!
                map["type"] = course.type!!
                database.getReference("attendance")
                    .child(id).setValue(map)
            }
        })
    }

    fun checkinStudent(mac: String, course: Course, id: String) {
        database.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (c in dataSnapshot.children) {
                    if (c.child("btAddress").value.toString() == mac && c.child("isStudent").value == true) {
                        if (c.child("checked").value.toString() == "false") {
                            val user = User(
                                userId = c.child("userId").value.toString().toInt(),
                                firstname = c.child("firstname").value.toString(),
                                middlename = c.child("middlename").value.toString(),
                                lastname = c.child("lastname").value.toString(),
                                isStudent = true,
                                isEmployee = false,
                                course = c.child("course").value.toString().toInt(),
                                group = c.child("group").value.toString(),
                                imei = c.child("imei").value.toString(),
                                btAddress = c.child("btAddress").value.toString()
                            )
                            addStudent(course, user, id)
                        }
                    }
                }
            }
        })
    }

    fun addStudent(course: Course, user: User, id: String) {
        database.getReference("courses").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                when (course.type) {
                    "courses" -> {
                        for (item in dataSnapshot.child(course.courseId!!).child("itemsList").children) {
                            if (item.value.toString() == user.course.toString()) {
                                database.getReference("attendance/" + id).child("students")
                                    .child(user.userId.toString()).setValue(
                                        user.lastname + " " +
                                                user.firstname + " " +
                                                user.middlename + " " +
                                                user.group
                                    )

                                database.getReference("users")
                                    .child(user.userId.toString()).child("checked").setValue("true")
                            }
                        }
                    }

                    "groups" -> {
                        for (item in dataSnapshot.child(course.courseId!!).child("itemsList").children) {
                            if (item.value.toString() == user.group.toString()) {
                                database.getReference("attendance/" + id).child("students")
                                    .child(user.userId.toString()).setValue(
                                        user.lastname + " " +
                                                user.firstname + " " +
                                                user.middlename + " " +
                                                user.group
                                    )

                                database.getReference("users")
                                    .child(user.userId.toString()).child("checked").setValue("true")
                            }
                        }
                    }

                    "students" -> {
                        for (item in dataSnapshot.child(course.courseId!!).child("itemsList").children) {
                            if (item.value.toString() == user.lastname + " " +
                                user.firstname + " " +
                                user.middlename + " " +
                                user.group
                            ) {
                                database.getReference("attendance/" + id).child("students")
                                    .child(user.userId.toString()).setValue(
                                        user.lastname + " " +
                                                user.firstname + " " +
                                                user.middlename + " " +
                                                user.group
                                    )

                                database.getReference("users")
                                    .child(user.userId.toString()).child("checked").setValue("true")
                            }
                        }
                    }
                }
            }
        })
    }
}