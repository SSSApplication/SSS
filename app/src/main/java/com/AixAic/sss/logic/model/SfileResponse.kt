package com.AixAic.sss.logic.model

//文件上传
data class GeneralResponse(val status: String)

data class SfileResponse(val status: String, val sfileList: List<Sfile>)

data class Sfile(val id: Int, val name: String, val type: String, val jid: Int)