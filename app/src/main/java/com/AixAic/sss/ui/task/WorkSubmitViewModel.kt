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
    val fromFiles = 3
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    var description = ""
    var jid = ""
    var remind = ""
    var sfileList = ArrayList<Sfile>()
    var picList = ArrayList<Sfile>()
    var accList = ArrayList<Sfile>()
    private val fileLiveData = MutableLiveData<RequestBody>()
    private val jidLiveData = MutableLiveData<Int>()
    private val idLiveData = MutableLiveData<Int>()
    private val jobJidLiveData = MutableLiveData<Int>()
    private val readLiveDate = MutableLiveData<Int>()

    val uploadLiveData = Transformations.switchMap(fileLiveData){body ->
        Repository.upload(body)
    }

    val listFileLiveData = Transformations.switchMap(jidLiveData){jid ->
        Repository.getByJid(jid)
    }

    val delteFileLiveData = Transformations.switchMap(idLiveData){ id ->
        Repository.delete(id)
    }

    val submitJobLiveData = Transformations.switchMap(jobJidLiveData){ id ->
        Repository.submit(id)
    }

    val readJobLiveData = Transformations.switchMap(readLiveDate){ id ->
        Repository.read(id)
    }

    //外部调用的接口
    fun upload(body: RequestBody){
        fileLiveData.value = body
    }

    fun listFile(jid: Int){
        jidLiveData.value = jid
    }

    fun deleteFile(id: Int) {
        idLiveData.value = id
    }

    fun submitJob(id: Int) {
        jobJidLiveData.value = id
    }

    fun readJob(id: Int){
        readLiveDate.value = id
    }

}