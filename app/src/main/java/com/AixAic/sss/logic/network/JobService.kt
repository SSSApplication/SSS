package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.JobResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JobService {
    @GET("job/{uid}")
    fun getJobList(@Path("uid") uid: Int): Call<JobResponse>

}