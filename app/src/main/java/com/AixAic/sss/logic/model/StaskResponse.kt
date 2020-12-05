package com.AixAic.sss.logic.model

data class StaskResponse(val status: String, val stask: Stask)

data class Stask(val id: Int, val uid: Int, val oid: Int, val title: String, val description: String, val filetype: String, val user: User){
}