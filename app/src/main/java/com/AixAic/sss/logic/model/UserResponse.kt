package com.AixAic.sss.logic.model

import com.AixAic.sss.util.LogUtil

//用户信息类
//status(返回状态) user（用户）
data class UserResponse (val status: String, val user: User)
//id， name(姓名), sno(学号), password(密码), school(学校), phone(电话),
data class User(val id: Int, val name: String, val sno: String, val password: String, val school: String, val phone: String, val organizationsList: List<Organizations>){
    fun isAdmin(): Boolean{
        for (organizations in this.organizationsList) {
            if (organizations.admin == 1) return true
        }
        return false
    }
}
//登陆时传输到服务器的数据
data class LoginData(val userName: String, val userPassword: String)

data class Organization(val id: Int, val name: String){
    override fun toString(): String {
        return this.name
    }
}

data class Organizations(val id: Int, val uid: Int, val oid: Int, val admin: Int, val organization: Organization)