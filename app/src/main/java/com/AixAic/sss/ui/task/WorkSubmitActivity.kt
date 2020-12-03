package com.AixAic.sss.ui.task

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.model.SfileResponse
import com.AixAic.sss.util.FileUtil
import com.AixAic.sss.util.HttpUtil
import com.AixAic.sss.util.LogUtil
import com.permissionx.aixlibrary.PermissionX
import kotlinx.android.synthetic.main.activity_work_submit.*
import java.io.File

class WorkSubmitActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(WorkSubmitViewModel::class.java) }

    private lateinit var pictureAdapter: PictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_submit)
        if (viewModel.description.isEmpty()){
            viewModel.description = intent.getStringExtra("description") ?: ""
            description.text = viewModel.description
        }
        if (viewModel.jid.isEmpty()){
            viewModel.jid = intent.getStringExtra("jid") ?: "0"

        }
        viewModel.listFile(viewModel.jid.toInt())
        LogUtil.d("jid", "${viewModel.jid}")
        add_pic_from_camera.setOnClickListener {
            //创建File对象，用于存储拍照后的照片
            viewModel.outputImage = File(SSSApplication.context.externalCacheDir, "output_image.jpg")
            if (viewModel.outputImage.exists()) {
                viewModel.outputImage.delete()
            }
            viewModel.outputImage.createNewFile()
            viewModel.imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                FileProvider.getUriForFile(SSSApplication.context, "com.AixAic.sss.fileprovider", viewModel.outputImage)
            } else {
                Uri.fromFile(viewModel.outputImage)
            }

            //启动相机服务
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.imageUri)
            intent.putExtra("imageUri", "${viewModel.imageUri}")
            LogUtil.d("imageUri", "${viewModel.imageUri}")
            startActivityForResult(intent, viewModel.takePhoto)
        }

        add_pic_from_photo.setOnClickListener {
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

        viewModel.uploadLiveData.observe(this, {result ->
            val generalResponse = result.getOrNull()
            if (generalResponse != null && generalResponse.status == "ok") {
                LogUtil.d("上传文件","成功")
                viewModel.listFile(viewModel.jid.toInt())
            }else {
                LogUtil.d("上传文件","失败")
            }
        })
        viewModel.listFileLiveData.observe(this, {result ->
            val sfileResponse = result.getOrNull()
            if (sfileResponse != null && sfileResponse.status == "ok") {
                LogUtil.d("获取文件","成功")
                viewModel.sfileList.clear()
                viewModel.sfileList.addAll(sfileResponse.sfileList)
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recyclePic.layoutManager = layoutManager
                pictureAdapter = PictureAdapter(viewModel.sfileList)
                LogUtil.d("listsize", viewModel.sfileList.size.toString())
                recyclePic.adapter = pictureAdapter
            }else{
                LogUtil.d("获取文件","失败")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            viewModel.takePhoto -> {
                LogUtil.d("上传文件大小", "${viewModel.outputImage.totalSpace}")
                val body = HttpUtil.imageUploadBody(viewModel.outputImage, viewModel.jid)
                viewModel.upload(body)
            }
            viewModel.fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val file = FileUtil.uri2file(SSSApplication.context, data.data!!)
                    LogUtil.d("上传文件", "${file.totalSpace}")
                    val body = HttpUtil.imageUploadBody(file, viewModel.jid)
                    viewModel.upload(body)
                }
            }
        }
    }
}