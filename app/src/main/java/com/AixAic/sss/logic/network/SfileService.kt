package com.AixAic.sss.logic.network


import com.AixAic.sss.logic.model.SfileResponse
import com.AixAic.sss.logic.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.http.*

interface SfileService {
    @Multipart
    @POST("sfile")
    fun upload(@Part("description") description: RequestBody, @Part file: MultipartBody.Part): Call<String>

    @POST("sfile")
    fun uploadnew(@Body body: RequestBody): Call<SfileResponse>

}