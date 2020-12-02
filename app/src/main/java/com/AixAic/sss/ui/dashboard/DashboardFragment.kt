package com.AixAic.sss.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.GeneralResponse
import com.AixAic.sss.logic.model.LoginData
import com.AixAic.sss.logic.network.ServiceCreator
import com.AixAic.sss.logic.network.SfileService
import com.AixAic.sss.util.FileUtil
import com.AixAic.sss.util.HttpUtil
import com.AixAic.sss.util.LogUtil
import com.permissionx.aixlibrary.PermissionX
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DashboardFragment : Fragment() {

    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    val viewModel by lazy { ViewModelProviders.of(this).get(DashboardViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        test.setOnClickListener {
            val loginData = LoginData("1777000074", "888888")
            val job = Job()
            val scope = CoroutineScope(job)
            scope.launch {
                Repository.login(loginData)
            }
            job.cancel()
        }
        takePhotoBtn.setOnClickListener {
            //创建File对象，用于存储拍照后的照片
            outputImage = File(SSSApplication.context.externalCacheDir, "output_image.jpg")
            if (outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                FileProvider.getUriForFile(SSSApplication.context, "com.AixAic.sss.fileprovider", outputImage)
            }else{
                Uri.fromFile(outputImage)
            }

            //启动相机服务
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            intent.putExtra("imageUri", "$imageUri")
            LogUtil.d("imageUri", "$imageUri")
            startActivityForResult(intent, takePhoto)
        }
        fromAlbumBtn.setOnClickListener {
            //申请读写文件的权限
            PermissionX.request(
                activity!!,
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
            startActivityForResult(intent, fromAlbum)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                LogUtil.d("上传成功", "${outputImage.totalSpace}")
                val body = HttpUtil.imageUploadBody(outputImage, "1")
                val uploadService = ServiceCreator.create<SfileService>()
                uploadService.upload(body).enqueue(object : Callback<GeneralResponse> {
                    override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                        val userResponse = response.body()
                        if (userResponse != null){
                            LogUtil.d("上传成功", "上传成功")
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        LogUtil.d("上传成功", "上传成功个鬼")
                        t.printStackTrace()
                    }
                })
                LogUtil.d("上传文件1", "${data?.data.toString()}")
                if (resultCode == Activity.RESULT_OK) {
                    //将拍摄的照片显示出来
                    val bitmap = BitmapFactory.decodeStream(SSSApplication.context.contentResolver.openInputStream(imageUri))
                    imageView.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    LogUtil.d("上传文件", "${data.data.toString()}")

                    val path = FileUtil.uri2path(SSSApplication.context, data.data!!)
                    val file = File(path)
                    LogUtil.d("上传文件", "${path}, ${file.totalSpace}")
                    val body = HttpUtil.imageUploadBody(file, "1")
                    val uploadService = ServiceCreator.create<SfileService>()
                    uploadService.upload(body).enqueue(object : Callback<GeneralResponse> {
                        override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                            val userResponse = response.body()
                            if (userResponse != null){
                                LogUtil.d("上传成功", "上传成功")
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                            LogUtil.d("上传成功", "上传成功个鬼")
                            t.printStackTrace()
                        }
                    })

                    data.data?.let {uri ->
                        //将图片显示
                        val bitmap = getBitmapFromUri(uri)
                        imageView.setImageBitmap(bitmap)
                    }
                }
                viewModel.testUploadnewLiveData.observe(this, {result ->
                    val an = result.getOrNull()
                    if (an != null){
                        LogUtil.d("上传文件","成功")
                    }else {
                        LogUtil.d("上传文件","失败")
                    }
                })
            }
        }
    }
    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }
    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle() //将不需要的Bitmap对象回收
        return rotatedBitmap
    }
    private fun getBitmapFromUri(uri: Uri) = SSSApplication.context.contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
}