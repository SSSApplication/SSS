package com.AixAic.sss.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.AixAic.sss.MainActivity
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication.Companion.context
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //记住密码功能
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        if (isRemember) {
            //将账号密码填入输入框内
            val userName = prefs.getString("userName", "")
            val userPassword = prefs.getString("userPassword", "")
            userNameEdit.setText(userName)
            userPasswordEdit.setText(userPassword)
            rememberPass.isChecked = true
        }
        loginBtn.setOnClickListener {
            val userName = userNameEdit.text.toString()
            val userPassword = userPasswordEdit.text.toString()
            if (userName == "1777000074" && userPassword == "123456"){
                if (rememberPass.isChecked) { //是否选中记住密码
                    prefs.edit() {
                        putBoolean("remember_password", true)
                        putString("userName", userName)
                        putString("userPassword", userPassword)
                    }
                }
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}