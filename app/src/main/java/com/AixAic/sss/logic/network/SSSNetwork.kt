package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.util.LogUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//定义一个统一的网络数据源访问入口，对所有网络请求的API进行封装
object SSSNetwork {
    //封装user的网络请求
    private val userService = ServiceCreator.create<UserService>()
    suspend fun login(loginData: LoginData) = userService.login(loginData).await()



    //执行请求
    //封装上传文件的网络请求
    private val sfileService = ServiceCreator.create<SfileService>()
    suspend fun upload(body: RequestBody) = sfileService.upload(body).await()

    //协程suspend
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->

            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    LogUtil.d("SSSNetwork", "服务器返回成功")
                    if (body != null) continuation.resume(body) //服务器返回成功
                    else continuation.resumeWithException(
                        //服务器返回成功，但是值为空
                        RuntimeException("response body is null 服务器返回成功，但是值为空")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    //服务器返回失败
                    LogUtil.d("SSSNetwork", "服务器返回失败")
                    continuation.resumeWithException(t)
                }
            })

        }
    }
}