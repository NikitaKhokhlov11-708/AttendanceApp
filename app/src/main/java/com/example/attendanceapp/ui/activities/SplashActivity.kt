package com.example.attendanceapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.attendanceapp.R
import com.example.attendanceapp.models.User
import com.google.gson.Gson


class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            var i = Intent(this@SplashActivity, LoginActivity::class.java)
            if (getSharedPreferences("Settings", Context.MODE_PRIVATE).contains("CurrentUser")) {
                val pref = getSharedPreferences("Settings", Context.MODE_PRIVATE)
                val gson = Gson()
                val json: String? = pref.getString("CurrentUser", "")
                val user: User = gson.fromJson<User>(json, User::class.java)

                if (user.isStudent!!)
                    i = Intent(this@SplashActivity, StudentActivity::class.java)
                else
                    i = Intent(this@SplashActivity, TeacherActivity::class.java)
            }

            startActivity(i)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
