package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    //登录
    @POST("login")
    fun login(@Body data: Data): Call<UserResponse>
}