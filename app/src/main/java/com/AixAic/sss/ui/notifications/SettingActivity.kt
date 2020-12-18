package com.AixAic.sss.ui.notifications

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.SSSApplication.Companion.context
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.ui.login.LoginActivity
import com.AixAic.sss.ui.task.CustomDialog
import com.AixAic.sss.util.FileUtil
import com.AixAic.sss.util.HttpUtil
import com.AixAic.sss.util.LogUtil
import com.permissionx.aixlibrary.PermissionX
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.setting_password
import kotlinx.android.synthetic.main.fragment_notifications.*

class SettingActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(SettingViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setting_return.setOnClickListener {
            onBackPressed()
        }
        setting_head.setOnClickListener {
            //申请读写文件的权限
            PermissionX.request(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) { allGranted, deniedList ->
                if (allGranted) {
                    Toast.makeText(
                        SSSApplication.context,
                        "All permissions are granted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        SSSApplication.context,
                        "You denied $deniedList",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定只显示图片
            intent.type = "image/*"
            startActivityForResult(intent, viewModel.fromAlbum)
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

        viewModel.uploadImgLiveData.observe(this, {result ->
            val generalResponse = result.getOrNull()
            if (generalResponse != null && generalResponse.status == "ok") {
                LogUtil.d("上传文件","成功")
            }else {
                LogUtil.d("上传文件","失败")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            viewModel.fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val file = FileUtil.uri2file(SSSApplication.context, data.data!!)
                    LogUtil.d("上传文件", "${file.totalSpace}")
                    val body = HttpUtil.userUploadBody(file, Repository.getUser().id.toString())
                    viewModel.uploadImg(body)
                }
            }
        }
    }

}