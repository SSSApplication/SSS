package com.AixAic.sss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Job
import okhttp3.RequestBody

class HomeViewModel : ViewModel() {

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