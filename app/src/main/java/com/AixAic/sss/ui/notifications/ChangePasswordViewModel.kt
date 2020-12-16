package com.AixAic.sss.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.AixAic.sss.logic.Repository

class ChangePasswordViewModel : ViewModel(){
    private val passwordLiveData = MutableLiveData<String>()

    val password = Repository.getUser().password
    val id = Repository.getUser().id

    val resultLiveData = Transformations.switchMap(passwordLiveData){ newPassword ->
        Repository.update(newPassword, id)
    }

    //外部调用接口
    fun update(newPassword: String) {
        passwordLiveData.value = newPassword
    }

}