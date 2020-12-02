package com.AixAic.sss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Job
import okhttp3.RequestBody

class HomeViewModel : ViewModel() {
    val submitAll = 1 //提交全部
    val submitted = 2 //已提交
    val noSubmit = 3 //未提交
    val receive = 4 //接收
    var status = submitAll


    val user = Repository.getUser()
    private val jobLiveData = MutableLiveData<Int>()
    var jobList = ArrayList<Job>()

    val jobResultLiveData = Transformations.switchMap(jobLiveData) {uid ->
        Repository.getJobList(uid)
    }

    fun refreshJobList(uid: Int) {
        jobLiveData.value = uid
    }
}