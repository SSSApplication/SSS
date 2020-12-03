package com.AixAic.sss.util

import android.widget.Toast
import com.AixAic.sss.SSSApplication

object ToastUtil {
    fun show(message: String){
        Toast.makeText(SSSApplication.context, message, Toast.LENGTH_SHORT).show()
    }
    fun showLong(message: String){
        Toast.makeText(SSSApplication.context, message, Toast.LENGTH_LONG).show()
    }
}