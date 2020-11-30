package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.GeneralResponse
import com.AixAic.sss.logic.model.Stask
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StaskService {
    @POST("stask")
    fun publishTask(@Body stask: Stask): Call<GeneralResponse>
}