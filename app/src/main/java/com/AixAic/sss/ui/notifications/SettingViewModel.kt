package com.AixAic.sss.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import okhttp3.RequestBody

class SettingViewModel : ViewModel() {
    val fromAlbum = 1
    private val fileLiveData = MutableLiveData<RequestBody>()

    val uploadImgLiveData = Transformations.switchMap(fileLiveData){body ->
        Repository.uploadImg(body)
    }

    fun uploadImg(body: RequestBody){
        fileLiveData.value = body
    }
}