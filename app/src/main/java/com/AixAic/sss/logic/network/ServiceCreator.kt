package com.AixAic.sss.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit构造器
object ServiceCreator {
//    "http://192.168.8.106/" 手机
//    模拟器 "http://10.0.2.2/"
    private const val BASE_URL = "http://10.0.2.2/"
    val BASE_IMG = BASE_URL+"uploaded/img/"
    val BASE_IMG_SMALL = BASE_URL + "uploaded/img_small/"
    val BASE_FILE = BASE_URL + "uploaded/file/"
    val BASE_PACKAGEFILE = BASE_URL + "packageFile/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) //所有请求的根目录
        .addConverterFactory(GsonConverterFactory.create()) //解析数据使用的转换库 --Gson直接转成对象
        .build()  //构建出Retrofit对象

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass) //创建出相应Service接口的动态代理对象
    //示例： val appservice = ServiceCreator.create(AppService::class.java)

    inline fun <reified T> create(): T = create(T::class.java) //泛型实化
    //示例： val appservice = ServiceCreator.create<AppService>()
}