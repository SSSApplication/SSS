package com.AixAic.sss.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.logic.model.User
import okhttp3.RequestBody

class NotificationsViewModel : ViewModel() {

    val fromAlbum = 1
    private val fileLiveData = MutableLiveData<RequestBody>()

    val uploadImgLiveData = Transformations.switchMap(fileLiveData){body ->
        Repository.uploadImg(body)
    }

    fun uploadImg(body: RequestBody){
        fileLiveData.value = body
    }
    private val userLiveData =MutableLiveData<LoginData>()

    var userName = ""
    var userPassword = ""
    val myselfLiveData = Transformations.switchMap(userLiveData){ loginData ->
        Repository.login(loginData)
    }

    //外部调用的接口
    fun refreshMyself(userName: String, userPassword: String){
        userLiveData.value = LoginData(userName, userPassword)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}