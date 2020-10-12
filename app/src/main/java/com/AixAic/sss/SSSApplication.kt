package com.AixAic.sss

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

//用于全局获取context
class SSSApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")  //这是Application的contxt，不是activity或service的，所以不存在内存泄漏，故注解。
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}