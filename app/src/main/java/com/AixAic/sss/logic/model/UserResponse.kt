package com.AixAic.sss.logic.model

//id， name(姓名), sno(学号), password(密码), school(学校), phone(电话)
data class UserResponse (val id: Int, val name: String, val sno: String, val password: String, val school: String, val phone: String)

data class Data(val test: String)