package com.AixAic.sss.ui.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication.Companion.context
import com.AixAic.sss.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setting_return.setOnClickListener {
            onBackPressed()
        }

        setting_password.setOnClickListener {
            val intent = Intent(context,ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        setting_change_user.setOnClickListener {
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }

        setting_exit.setOnClickListener {
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}