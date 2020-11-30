package com.AixAic.sss.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Stask
import com.AixAic.sss.util.LogUtil

class WorkPublishViewModel : ViewModel() {
    private val staskLiveData = MutableLiveData<Stask>()

    var id = 0
    var oid = 0
    var description = ""
    var filetype = ""
    val uid = Repository.getUser().id
    val resultLiveData = Transformations.switchMap(staskLiveData){ stask ->
        Repository.publishTask(stask)
    }

    //外部调用接口
    fun publishWork(id: Int, uid: Int, oid: Int, description: String, filetype: String) {
        staskLiveData.value = Stask(id, uid, oid, description, filetype)
    }
}