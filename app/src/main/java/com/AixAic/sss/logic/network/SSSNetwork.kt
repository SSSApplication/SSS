package com.AixAic.sss.logic.network

import com.AixAic.sss.logic.model.LoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//定义一个统一的网络数据源访问入口，对所有网络请求的API进行封装
object SSSNetwork {
    //封装user的网络请求
    private val userService = ServiceCreator.create<UserService>()
    suspend fun login(loginData: LoginData) = userService.login(loginData).await()


    //协程suspend
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body) //服务器返回成功
                    else continuation.resumeWithException(
                        //服务器返回成功，但是值为空
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    //服务器返回失败
                    continuation.resumeWithException(t)
                }
            })

        }
    }
}