package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.GeneralResponse
import com.AixAic.sss.logic.model.Stask
import com.AixAic.sss.logic.model.StaskResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StaskService {
    @POST("stask")
    fun publishTask(@Body stask: Stask): Call<GeneralResponse>

    @GET("stask/{id}")
    fun packageFile(@Path("id") id: Int): Call<StaskResponse>

}