package com.AixAic.sss.ui.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.ui.BottomActivity
import com.AixAic.sss.ui.login.LoginActivity
import com.AixAic.sss.util.ToastUtil
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        password_return.setOnClickListener {
            onBackPressed()
        }

        changePassword.setOnClickListener {
            val oldPassword = oldPassword.text.toString()
            val newPassword = newPassword.text.toString()
            if (oldPassword != "" && newPassword != "") {
                if (oldPassword == viewModel.password) {
                    viewModel.update(newPassword)
                }else {
                    ToastUtil.show("密码错误")
                }
            }else {
                ToastUtil.show("密码不能为空")
            }
            viewModel.resultLiveData.observe(this, { result ->
                val passwordResponse = result.getOrNull()
                if (passwordResponse != null) {
                    Repository.saveUser(passwordResponse)
                    val intent = Intent(SSSApplication.context, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show()
                    result.exceptionOrNull()?.printStackTrace()
                }
            })
        }
    }
}