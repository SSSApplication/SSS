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
    private val packageLiveData = MutableLiveData<Int>()
    private val remindLiveData = MutableLiveData<Int>()
    private val remindAllLiveData = MutableLiveData<Int>()
    var jobList = ArrayList<Job>()

    val receiveResultLiveData = Transformations.switchMap(receiveLiveData) {stid ->
        Repository.getListByStid(stid)
    }

    val packageFileLiveData = Transformations.switchMap(packageLiveData) {id ->
        Repository.packageFile(id)
    }

    val remindResultLiveData = Transformations.switchMap(remindLiveData) {id ->
        Repository.remind(id)
    }

    val remindAllResultLiveData = Transformations.switchMap(remindAllLiveData) {stid ->
        Repository.remindAll(stid)
    }

    fun refreshJobList(stid: Int) {
        receiveLiveData.value = stid
    }

    fun packageFile(id: Int) {
        packageLiveData.value = id
    }

    fun remind(id: Int) {
        remindLiveData.value = id
    }

    fun remindAll(stid: Int) {
        remindAllLiveData.value = stid
    }

}