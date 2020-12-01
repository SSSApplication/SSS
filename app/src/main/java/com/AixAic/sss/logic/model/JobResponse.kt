package com.AixAic.sss.logic.model

data class JobResponse(val status: String, val jobList: List<Job>)

data class Job(val id: Int, val stid: Int, val status: Int, val uid: Int, val user: User, val stask: Stask)