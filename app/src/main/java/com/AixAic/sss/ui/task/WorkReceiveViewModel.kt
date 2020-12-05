package com.AixAic.sss.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Job

class WorkReceiveViewModel : ViewModel(){
    var description = ""
    var stid = ""
    val submitAll = 1 //提交全部
    val submitted = 2 //已提交
    val noSubmit = 3 //未提交
    var status = submitAll

    val user = Repository.getUser()
    private val receiveLiveData = MutableLiveData<Int>()
    var jobList = ArrayList<Job>()

    val receiveResultLiveData = Transformations.switchMap(receiveLiveData) {stid ->
        Repository.getListByStid(stid)
    }

    fun refreshJobList(stid: Int) {
        receiveLiveData.value = stid
    }

}