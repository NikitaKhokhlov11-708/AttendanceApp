package com.example.attendanceapp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendanceapp.api.KpfuApiService
import com.example.attendanceapp.api.Student
import com.example.attendanceapp.database.Database
import com.example.attendanceapp.models.User
import com.example.attendanceapp.views.LoginView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>() {
    var params: Pair<String, String> = Pair("", "")
    var imeiP: String = ""
    val database = Database()
    var btAddress = ""

    fun onLoginClick() {
        viewState.getBtAddress()
        viewState.showProgress()
        viewState.getText()
        viewState.getImei()

        lateinit var user: User

        if (params.first == "test" && params.second == "test") {
            user = User(
                123456,
                "Иван",
                "Иванович",
                "Иванов",
                false,
                true,
                0,
                "",
                imeiP,
                btAddress
            )

            addUser(user)
        } else {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://shelly.kpfu.ru/e-ksu/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            val userService: KpfuApiService =
                retrofit.create<KpfuApiService>(KpfuApiService::class.java)
            userService.getUser(params.first, params.second).enqueue(object : Callback<Student> {
                override fun onFailure(call: Call<Student>, t: Throwable) {
                    viewState.sendErrorToast()
                }

                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    when (response.body()?.successful) {
                        true -> {
                            user = User(
                                response.body()?.user_id,
                                response.body()?.firstname,
                                response.body()?.middlename,
                                response.body()?.lastname,
                                response.body()?.student,
                                response.body()?.employee,
                                response.body()?.student_info!!.student_current_course,
                                response.body()?.student_info!!.student_group_name,
                                imeiP,
                                btAddress
                            )

                            addUser(user)
                        }
                        false -> viewState.sendErrorToast()
                    }
                }
            })
        }

        viewState.hideProgress()
    }

    fun getText(logpass: Pair<String, String>) {
        params = logpass
    }

    fun getImei(imei: String) {
        imeiP = imei
    }

    fun getBtAddress(btMac: String) {
        btAddress = btMac
    }

    fun addUser(user: User) {
        if (user != database.checkUser(user.userId.toString(), imeiP)) {
            database.addUser(user)
        }
        viewState.saveUser(user)
        var type = "default"
        if (user.isStudent == true)
            type = "student"
        else if (user.isEmployee == true)
            type = "employee"
        viewState.login(type)
    }
}