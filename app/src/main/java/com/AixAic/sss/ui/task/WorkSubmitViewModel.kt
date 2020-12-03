package com.AixAic.sss.ui.task

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Sfile
import okhttp3.RequestBody
import java.io.File

class WorkSubmitViewModel : ViewModel() {
    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    var description = ""
    var jid = ""
    var sfileList = ArrayList<Sfile>()
    private val fileLiveData = MutableLiveData<RequestBody>()
    private val jidLiveData = MutableLiveData<Int>()

    val uploadLiveData = Transformations.switchMap(fileLiveData){body ->
        Repository.upload(body)
    }

    val listFileLiveData = Transformations.switchMap(jidLiveData){jid ->
        Repository.getByJid(jid)
    }

    //外部调用的接口
    fun upload(body: RequestBody){
        fileLiveData.value = body
    }

    fun listFile(jid: Int){
        jidLiveData.value = jid
    }



}