package com.example.attendanceapp.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.LoginPresenter
import com.example.attendanceapp.views.LoginView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : MvpAppCompatActivity(), LoginView {

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @ProvidePresenter
    fun provideLoginPresenter() = LoginPresenter()

    var imei = ""

    private val REQUEST_PERMISSION_PHONE_STATE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_PHONE_STATE
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                REQUEST_PERMISSION_PHONE_STATE
            )
        }

        btnLogin.setOnClickListener { loginPresenter.onLoginClick() }
    }

    override fun login(type: String) {
        lateinit var intent: Intent
        if (type == "student") {
            intent = Intent(this@LoginActivity, StudentActivity::class.java)
            startActivity(intent)
        } else if (type == "employee") {
            intent = Intent(this@LoginActivity, TeacherActivity::class.java)
            startActivity(intent)
        } else
            Toast.makeText(
                baseContext,
                "Ошибка! Приложение доступно только для студентов и преподавателей",
                Toast.LENGTH_LONG
            ).show()
    }

    override fun getText() {
        loginPresenter.getText(Pair(etLogin.text.toString(), etPassword.text.toString()))
    }

    override fun sendErrorToast() {
        Toast.makeText(baseContext, "Ошибка", Toast.LENGTH_LONG).show()
    }

    override fun sendTestToast(data: String) {
        Toast.makeText(baseContext, data, Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getImei() {
        val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_PHONE_STATE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                REQUEST_PERMISSION_PHONE_STATE
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = tel.imei
            } else {
                imei = tel.deviceId
            }
        }

        loginPresenter.getImei(imei)
    }

    override fun getBtAddress() {
        val SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address"

        val macAddress: String = Settings.Secure.getString(
            contentResolver,
            SECURE_SETTINGS_BLUETOOTH_ADDRESS
        )

        loginPresenter.getBtAddress(macAddress)
    }

    override fun saveUser(user: User) {
        val mPrefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("CurrentUser", json)
        prefsEditor.apply()
    }

    override fun showProgress() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,

        builder.setView(R.layout.layout_loading_dialog)
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    override fun hideProgress() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,

        builder.setView(R.layout.layout_loading_dialog)
        val dialog: AlertDialog = builder.create()

        dialog.hide()
    }
}
