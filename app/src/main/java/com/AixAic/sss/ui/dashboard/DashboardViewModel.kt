package com.AixAic.sss.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import okhttp3.RequestBody

class DashboardViewModel : ViewModel() {

    private val uploadLiveData = MutableLiveData<String>()
    //外部调用的接口
    fun uploadmv(sfileurl: String){
        uploadLiveData.value = sfileurl
    }


    private val uploadnewLiveData = MutableLiveData<RequestBody>()

    val testUploadnewLiveData = Transformations.switchMap(uploadnewLiveData){ body ->
        Repository.upload(body)
    }
    //外部调用的接口
    fun uploadnew(body: RequestBody){
        uploadnewLiveData.value = body
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}