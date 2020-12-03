package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.GeneralResponse
import com.AixAic.sss.logic.model.JobResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface JobService {
    @GET("job/{uid}")
    fun getJobList(@Path("uid") uid: Int): Call<JobResponse>

//    提交
    @PUT("job/{id}")
    fun submit(@Path("id") id: Int): Call<GeneralResponse>
}