package com.AixAic.sss.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.AixAic.sss.MainActivity
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication.Companion.context
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            val userName = userName.text.toString()
            val userPassword = userPassword.text.toString()
            if (userName == "admin" && userPassword == "123456"){
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }

}