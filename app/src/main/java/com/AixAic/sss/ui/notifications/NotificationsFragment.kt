package com.AixAic.sss.ui.notifications

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.User
import com.AixAic.sss.logic.network.ServiceCreator
import com.AixAic.sss.ui.login.LoginActivity
import com.AixAic.sss.ui.task.WorkPublishActivity
import com.AixAic.sss.util.FileUtil
import com.AixAic.sss.util.HttpUtil
import com.AixAic.sss.util.LogUtil
import com.AixAic.sss.util.ToastUtil
import com.bumptech.glide.Glide
import com.permissionx.aixlibrary.PermissionX
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(NotificationsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = Repository.getUser()

        if (viewModel.userName.isEmpty()) viewModel.userName = user.sno
        if (viewModel.userPassword.isEmpty()) viewModel.userPassword = user.password
        viewModel.myselfLiveData.observe(this, {result ->
            val user = result.getOrNull()
            if (user != null){
                showUserInfo(user)
                Repository.saveUser(user)
            }else {
                Toast.makeText(context, "登陆已过期请重新登录！", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false

        })
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        headPicture.setOnClickListener {
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
            startActivityForResult(intent, viewModel.fromAlbum)
        }

        setting.setOnClickListener {
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        }

        viewModel.uploadImgLiveData.observe(this, {result ->
            val generalResponse = result.getOrNull()
            if (generalResponse != null && generalResponse.status == "ok") {
                LogUtil.d("上传文件","成功")
                swipeRefresh.isRefreshing = false

                refreshWeather()

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

    fun refreshWeather() {
        var imgurl = ServiceCreator.BASE_USER_IMG+Repository.getUser().id+".jpg"+"?"+(Math.random()*100).toInt()
        Glide.with(SSSApplication.context).load(imgurl).into(headPicture)
        LogUtil.d("啦啦啦",imgurl)
        viewModel.refreshMyself(viewModel.userName, viewModel.userPassword)
        swipeRefresh.isRefreshing = true
    }

    fun showUserInfo(user: User){
        LogUtil.d("Login", "已登录")
        userName.text = user.name
        userSno.text = user.sno
        school.text = user.school
        organization.text = user.organizationsList[0].organization.name
        if (viewModel.userName.isEmpty()) viewModel.userName = user.sno
        if (viewModel.userPassword.isEmpty()) viewModel.userPassword = user.password
    }
}