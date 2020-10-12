package com.AixAic.sss.logic.model
//用户信息类
//id， name(姓名), sno(学号), password(密码), school(学校), phone(电话)
data class UserResponse (val id: Int, val name: String, val sno: String, val password: String, val school: String, val phone: String)
//登陆时传输到服务器的数据
data class LoginData(val userName: String, val userPassword: String)