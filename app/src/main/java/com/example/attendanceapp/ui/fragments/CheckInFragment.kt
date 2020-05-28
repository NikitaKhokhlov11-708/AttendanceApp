package com.example.attendanceapp.ui.fragments

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.CheckInPresenter
import com.example.attendanceapp.views.CheckInView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_checkin.view.*

class CheckInFragment : MvpAppCompatFragment(), CheckInView {

    @InjectPresenter
    lateinit var checkInPresenter: CheckInPresenter

    @ProvidePresenter
    fun provideCheckInPresenter() = CheckInPresenter()

    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    lateinit var btDialog: AlertDialog

    private var root: View? = null

    companion object {
        fun newInstance(): CheckInFragment =
            CheckInFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_checkin, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setTitle("Идет учет посещаемости...")
        builder.setNeutralButton("Завершить") { dialog, which ->
            mBluetoothAdapter.cancelDiscovery()
            mBluetoothAdapter.disable()
            dialog.cancel()
        }

        builder.setView(R.layout.layout_loading_dialog)
        btDialog = builder.create()

        root!!.btnCheckIn.setOnClickListener {
            checkIn()
        }

        getCurrentUser()

        return root
    }

    override fun getCurrentUser() {
        val mPrefs = this.activity!!.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString("CurrentUser", "")
        val user: User = gson.fromJson<User>(json, User::class.java)

        checkInPresenter.getUser(user)
    }

    override fun sendToast() {
        mBluetoothAdapter.cancelDiscovery()
        mBluetoothAdapter.disable()
        Toast.makeText(activity!!.baseContext, "Поздравляем, Вас отметили!", Toast.LENGTH_LONG)
            .show()
    }

    override fun checkIn() {
        checkInPresenter.scanDatabase(btDialog)
        startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1)
        mBluetoothAdapter.startDiscovery()
        btDialog.show()
    }
}