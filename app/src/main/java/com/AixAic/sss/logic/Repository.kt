package com.AixAic.sss.logic

import androidx.lifecycle.liveData
import com.AixAic.sss.logic.dao.UserDAO
import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.logic.model.Stask
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
        if (userResponse.status == "ok") { //根据状态来处理
            LogUtil.d("登录模块", "登录成功，用户名：${userResponse.user.name}")
            val user = userResponse.user
            Result.success(user)
        } else {
            LogUtil.d("登录模块", "登录失败，${userResponse.status}")
            Result.failure(RuntimeException("response status is ${userResponse.status}"))
        }
    }

//上传文件
    fun upload(body: RequestBody) = fire(Dispatchers.IO) {
        val generalResponse = SSSNetwork.upload(body)
        if (generalResponse.status == "ok"){
            LogUtil.d("上传文件模块", "上传成功")
            Result.success(generalResponse)
        } else{
            LogUtil.d("上传文件模块", "上传失败")
            Result.failure(RuntimeException("response status is ${generalResponse.status}"))
        }
    }

    //获取文件
    fun getByJid(jid: Int) = fire(Dispatchers.IO) {
        val sfileResponse = SSSNetwork.listByJid(jid)
        if (sfileResponse.status == "ok") {
            LogUtil.d("获取文件模块", "获取成功")
            Result.success(sfileResponse)
        }else{
            LogUtil.d("获取文件模块", "获取失败")
            Result.failure(RuntimeException("response status is ${sfileResponse.status}"))
        }
    }

    fun getListByStid(stid: Int) = fire(Dispatchers.IO) {
        val jobResponse = SSSNetwork.getListByStid(stid)
        if (jobResponse.status == "ok"){
            LogUtil.d("获取文件模块(stid)", "获取成功")
            Result.success(jobResponse)
        }else{
            LogUtil.d("获取文件模块(stid)", "获取失败")
            Result.failure(RuntimeException("response status is ${jobResponse.status}"))
        }
    }
    //删除文件
    fun delete(id: Int) = fire(Dispatchers.IO) {
        val generalResponse = SSSNetwork.delete(id)
        if (generalResponse.status == "ok") {
            LogUtil.d("删除文件模块", "删除成功")
            Result.success(generalResponse)
        }else{
            LogUtil.d("删除文件模块", "删除失败")
            Result.failure(RuntimeException("response status is ${generalResponse.status}"))
        }
    }

    //发布任务
    fun publishTask(stask: Stask) = fire(Dispatchers.IO) {
        val generalResponse = SSSNetwork.publishTask(stask)
        if (generalResponse.status == "ok") {
            LogUtil.d("发布作业模块", "发布成功")
            Result.success(generalResponse)
        } else{
            LogUtil.d("发布作业模块", "发布失败${generalResponse.status}")
            Result.failure(java.lang.RuntimeException("response status is ${generalResponse.status}"))
        }
    }

    fun packageFile(id: Int) = fire(Dispatchers.IO) {
        val staskResponse = SSSNetwork.packageFile(id)
        if (staskResponse.status == "ok") {
            LogUtil.d("打包下载模块", "打包成功")
            Result.success(staskResponse)
        }else{
            LogUtil.d("打包下载模块", "打包失败${staskResponse.status}")
            Result.failure(java.lang.RuntimeException("response status is ${staskResponse.status}"))
        }
    }

    //获取jobList
    fun getJobList(uid: Int) = fire(Dispatchers.IO) {
        val jobResponse = SSSNetwork.getJobList(uid)
        if (jobResponse.status == "ok") {
            LogUtil.d("获取job模块", "获取成功")
            Result.success(jobResponse)
        } else {
            LogUtil.d("获取job模块", "获取失败${jobResponse.status}")
            Result.failure(java.lang.RuntimeException("response status is ${jobResponse.status}"))
        }
    }
    //提交作业
    fun submit(id: Int) = fire(Dispatchers.IO) {
        val generalResponse = SSSNetwork.submit(id)
        if (generalResponse.status == "ok"){
            LogUtil.d("提交job模块", "提交成功")
            Result.success(generalResponse)
        } else {
            LogUtil.d("提交job模块", "提交失败${generalResponse.status}")
            Result.failure(java.lang.RuntimeException("response status is ${generalResponse.status}"))
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