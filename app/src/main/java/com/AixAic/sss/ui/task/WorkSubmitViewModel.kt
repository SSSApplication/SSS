package com.AixAic.sss.ui.task

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import okhttp3.RequestBody
import java.io.File

class WorkSubmitViewModel : ViewModel() {
    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    var description = ""
    var jid = ""
    private val fileLiveData = MutableLiveData<RequestBody>()
    val uploadLiveData = Transformations.switchMap(fileLiveData){body ->
        Repository.upload(body)
    }

    //外部调用的接口
    fun upload(body: RequestBody){
        fileLiveData.value = body
    }


}