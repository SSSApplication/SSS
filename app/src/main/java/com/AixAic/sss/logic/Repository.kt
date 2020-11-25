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
            LogUtil.d("Login", userResponse.status)
            LogUtil.d("Login", userResponse.user.name)
        }
        if (userResponse.status == "ok") { //根据状态来处理
            val user = userResponse.user
            Result.success(user)
        } else {
            Result.failure(RuntimeException("response status is ${userResponse.status}"))
        }
    }


    fun uploadnew(body: RequestBody) = fire(Dispatchers.IO) {
        val requestBody = SSSNetwork.uploadnew(body)
        if (requestBody != null){
            LogUtil.d("uploadnew", "上传成功")
            Result.success(requestBody)
        }else {
            LogUtil.d("uploadnew", "失败")
            Result.failure(RuntimeException("response status is false"))
        }
    }

//    上传文件封装
    fun upload(fileurl: String) = fire(Dispatchers.IO) {
        LogUtil.d("Login", "上传文件1111$fileurl")
        val file = File(fileurl)
//    创建RequeBody，用于封装构建RequestBody
        val requestFile  = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//    MultipartBody.Part 和后端约定好key，这里是的partName是用image
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//    添加描述
        val descriptionString = "hello,这是描述"
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
        val requestBody = SSSNetwork.upload(description, body)
        if (requestBody != null){
            LogUtil.d("Login", "上传成功")
            Result.success(requestBody)
        }else {
            LogUtil.d("Login", "失败")
            Result.failure(RuntimeException("response status is false"))
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