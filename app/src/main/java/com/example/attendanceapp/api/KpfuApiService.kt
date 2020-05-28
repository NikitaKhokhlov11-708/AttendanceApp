package com.example.attendanceapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface KpfuApiService {

    @GET("portal_pg_mobile.authentication")
    fun getUser(
        @Query("p_login") login: String,
        @Query("p_pass") password: String
    ): Call<Student>
}