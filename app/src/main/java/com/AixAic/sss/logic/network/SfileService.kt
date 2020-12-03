package com.AixAic.sss.logic.network


import com.AixAic.sss.logic.model.GeneralResponse
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.http.*

interface SfileService {
    //上传文件
    @POST("sfile")
    fun upload(@Body body: RequestBody): Call<GeneralResponse>

    //获取文件列表
    @GET("sfile/{jid}")
    fun listByjid(@Path("jid") jid: Int): Call<GeneralResponse>
}