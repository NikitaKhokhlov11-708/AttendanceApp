package com.example.attendanceapp.presenters

import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.CheckInView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@InjectViewState
class CheckInPresenter : MvpPresenter<CheckInView>() {

    lateinit var cUser: User
    val database = Database()

    fun getUser(user: User) {
        cUser = user
    }

    fun scanDatabase(btDialog: AlertDialog) {
        database.database.getReference("users").child(cUser.userId.toString()).child("checked")
            .setValue("false")

        database.database.getReference("users").child(cUser.userId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.child("checked").value.toString() == "true") {
                        btDialog.cancel()
                        viewState.sendToast()
                    }
                }
            })
    }
}