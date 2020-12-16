package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.logic.model.User
import com.AixAic.sss.logic.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    //登录
    @POST("userlogin")
    fun login(@Body loginData: LoginData): Call<UserResponse>

    @GET("users/1")
    fun get(): Call<User>

    @PUT("users/{id}")
    fun update(@Body password: String, @Path("id") id: Int): Call<UserResponse>

}