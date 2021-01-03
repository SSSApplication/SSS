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
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.bumptech.glide.Glide
import com.permissionx.aixlibrary.PermissionX
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URL

class DashboardFragment : Fragment() {
    private val dynamicList = ArrayList<Dynamic>()

    val viewModel by lazy { ViewModelProviders.of(this).get(DashboardViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDynamic()
        val layoutManager = LinearLayoutManager(activity)
        dynamicRecycler.layoutManager = layoutManager
        val adapter = DynamicAdapter(dynamicList)
        dynamicRecycler.adapter = adapter
    }

    private fun initDynamic() {
        dynamicList.add(Dynamic("成先杰", "借用他人的大脑智慧，观察他人的战略布局，学习他人的思维方式，拎重点，用以指导自己的投资实践"))
        dynamicList.add(Dynamic("谢小雪", "感谢青年大学习让我体会到五分钟的漫长！"))
        dynamicList.add(Dynamic("李华", "我今天下午坐在马路边把学习强国给刷完了。"))
        dynamicList.add(Dynamic("成先杰", "图书馆人多的时候就很温暖，果然还是要大家一起学习"))
        dynamicList.add(Dynamic("李华", "有没有好的英语学习软件或者背单词软件推荐啊？？？"))
        dynamicList.add(Dynamic("李华", "想学习修图批图美图，上色润色的我可以带你们一下，最近开了直播课，无偿的，有无经验都行。"))
        dynamicList.add(Dynamic("李华", "我今天下午坐在马路边把学习强国给刷完了。"))
    }
}