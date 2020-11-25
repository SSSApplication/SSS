package com.AixAic.sss.logic

import androidx.lifecycle.liveData
import com.AixAic.sss.logic.dao.UserDAO
import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.logic.model.User
import com.AixAic.sss.logic.network.SSSNetwork
import com.AixAic.sss.util.LogUtil
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.coroutines.CoroutineContext
//仓库层的统一封装入口
object Repository {

    //获取用户 Dispatchers.IO函数线程类型设置，里面的代码全在子线程运行
    fun login(loginData: LoginData) = fire(Dispatchers.IO) {
        val userResponse = SSSNetwork.login(loginData)
        if (userResponse != null){
            LogUtil.d("登录模块", "登录成功，用户名：${userResponse.user.name}")
        }
        if (userResponse.status == "ok") { //根据状态来处理
            val user = userResponse.user
            Result.success(user)
        } else {
            LogUtil.d("登录模块", "登录失败，${userResponse.status}")
            Result.failure(RuntimeException("response status is ${userResponse.status}"))
        }
    }


    fun upload(body: RequestBody) = fire(Dispatchers.IO) {
        val generalResponse = SSSNetwork.upload(body)
        if (generalResponse != null){
            LogUtil.d("上传文件模块", "上传成功")
            Result.success(generalResponse)
        }else {
            LogUtil.d("上传文件模块", "上传失败")
            Result.failure(RuntimeException("response status is ${generalResponse.status}"))
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
//    用户操作封装
    fun saveUser(user: User) = UserDAO.saveUser(user)
    fun getUser() = UserDAO.getUser()
    fun isUserSaved() = UserDAO.isUserSaved()
}