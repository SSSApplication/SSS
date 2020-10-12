package com.AixAic.sss.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.model.User
import com.google.gson.Gson

//用户的SharedPerferences
object UserDAO {

    //存储用户
    fun saveUser(user: User) {
        sharedPreferences().edit {
            putString("user", Gson().toJson(user))
        }
    }

    //获取用户
    fun getUser(): User {
        val placeJson = sharedPreferences().getString("user", "")
        return Gson().fromJson(placeJson, User::class.java)
    }
    //用户是否存在
    fun isUserSaved() = sharedPreferences().contains("user")
    //获取SP
    private fun sharedPreferences() = SSSApplication.context.getSharedPreferences("SSS", Context.MODE_PRIVATE)
}