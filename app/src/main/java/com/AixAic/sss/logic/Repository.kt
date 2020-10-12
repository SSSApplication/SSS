package com.AixAic.sss.logic

import androidx.lifecycle.liveData
import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.logic.network.SSSNetwork
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
//仓库层的统一封装入口
object Repository {


    //获取用户 Dispatchers.IO函数线程类型设置，里面的代码全在子线程运行
    fun login(loginData: LoginData) = fire(Dispatchers.IO) {
        val userResponse = SSSNetwork.login(loginData)
        if (userResponse.status == "ok") { //根据状态来处理
            val user = userResponse.user
            Result.success(user)
        } else {
            Result.failure(RuntimeException("response status is ${userResponse.status}"))
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }
}