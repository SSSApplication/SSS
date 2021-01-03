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
import com.AixAic.sss.logic.model.Sfile
import com.AixAic.sss.logic.model.SfileResponse
import com.AixAic.sss.ui.BottomActivity
import com.AixAic.sss.ui.home.HomeFragment
import com.AixAic.sss.util.FileUtil
import com.AixAic.sss.util.HttpUtil
import com.AixAic.sss.util.LogUtil
import com.AixAic.sss.util.ToastUtil
import com.permissionx.aixlibrary.PermissionX
import kotlinx.android.synthetic.main.activity_work_submit.*
import java.io.File

class WorkSubmitActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(WorkSubmitViewModel::class.java) }
    lateinit var customDialog: CustomDialog
    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var accessoryAdapter: AccessoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.rightin_enter,R.anim.rightin_exit)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_submit)
        customDialog = CustomDialog(this)

        if (viewModel.description.isEmpty()){
            viewModel.description = (intent.getStringExtra("description") ?: "") + "\n" + "提交格式："+(intent.getStringExtra("fileType") ?: "")
            description.text = viewModel.description
        }
        if (viewModel.jid.isEmpty()){
            viewModel.jid = intent.getStringExtra("jid") ?: "0"
        }
        if (viewModel.remind.isEmpty()){
            viewModel.remind = intent.getStringExtra("remind") ?: "0"
        }
        viewModel.description = (intent.getStringExtra("description") ?: "") + "\n" + "提交格式："+(intent.getStringExtra("fileType") ?: "")
        viewModel.jid = intent.getStringExtra("jid") ?: "0"
        viewModel.remind = intent.getStringExtra("remind") ?: "0"
        if (viewModel.remind == "1"){
            viewModel.readJob(viewModel.jid.toInt())
        }
        viewModel.listFile(viewModel.jid.toInt())
        LogUtil.d("jid", "${viewModel.jid}")

        submit_return.setOnClickListener {
            onBackPressed()
        }

//        照相上传图片
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
//       从文件上传图片
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
//        上传附件
        add_accessory.setOnClickListener {
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
            intent.type = "*/*"
            startActivityForResult(intent, viewModel.fromFiles)
        }
//        提交作业
        workSubmitBtn.setOnClickListener {
            if (viewModel.sfileList.size == 0){
                ToastUtil.show("不能提交空作业")
            }else {
                viewModel.submitJob(viewModel.jid.toInt())
            }
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
                sortSfile(sfileResponse.sfileList)
//                这里是图片的
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recyclePic.layoutManager = layoutManager
                pictureAdapter = PictureAdapter(this, viewModel.picList)
                LogUtil.d("picList", viewModel.picList.size.toString())
                recyclePic.adapter = pictureAdapter
//                这里是附件的
                val layoutManagerAcc = LinearLayoutManager(this)
                layoutManagerAcc.orientation = LinearLayoutManager.VERTICAL
                recycleAcc.layoutManager = layoutManagerAcc
                accessoryAdapter = AccessoryAdapter(this, viewModel.accList)
                LogUtil.d("accList", viewModel.accList.size.toString())
                recycleAcc.adapter = accessoryAdapter
                customDialog.dismiss()

            }else{
                LogUtil.d("获取文件","失败")
                customDialog.dismiss()
            }
        })
        viewModel.delteFileLiveData.observe(this, { result ->
            val generalResponse = result.getOrNull()
            if (generalResponse != null && generalResponse.status == "ok"){
                viewModel.listFile(viewModel.jid.toInt())
            }else {
                LogUtil.d("删除文件","失败")
            }
        })
        viewModel.submitJobLiveData.observe(this, {result ->
            val generalResponse = result.getOrNull()
            if (generalResponse != null && generalResponse.status == "ok"){
                val intent = Intent(this, BottomActivity::class.java)
                startActivity(intent)
            }else {
                LogUtil.d("提交作业失败","失败")
                ToastUtil.show("提交作业失败")
            }
        })
        viewModel.readJobLiveData.observe(this, {result ->
            val generalResponse = result.getOrNull()
            if (generalResponse != null && generalResponse.status == "ok"){
                LogUtil.d("已读成功","已读成功")
            }else {
                LogUtil.d("已读","失败")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            viewModel.takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    customDialog.show()
                    LogUtil.d("上传文件大小", "${viewModel.outputImage.totalSpace}")
                    val body = HttpUtil.imageUploadBody(viewModel.outputImage, viewModel.jid)
                    viewModel.upload(body)
                }
            }
            viewModel.fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    customDialog.show()
                    val file = FileUtil.uri2file(SSSApplication.context, data.data!!)
                    LogUtil.d("上传文件", "${file.totalSpace}")
                    val body = HttpUtil.imageUploadBody(file, viewModel.jid)
                    viewModel.upload(body)
                }
            }
            viewModel.fromFiles -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    customDialog.show()
                        val file = FileUtil.uri2file(SSSApplication.context, data.data!!)
                        LogUtil.d("上传文件", "${file.totalSpace}")
                        val body = HttpUtil.fileUploadBody(file, viewModel.jid)
                        viewModel.upload(body)
                    }
            }
        }
    }

    private fun sortSfile(sfileList: List<Sfile>) {
        viewModel.picList.clear()
        viewModel.accList.clear()
        viewModel.sfileList.clear()
        viewModel.sfileList.addAll(sfileList)
        for (sfile in sfileList){
            if (sfile.type == "image/*") viewModel.picList.add(sfile)
            else viewModel.accList.add(sfile)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.rightout_enter,R.anim.rightout_exit)
    }
}