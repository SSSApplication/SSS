package com.AixAic.sss.ui.task

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.model.Job
import com.AixAic.sss.logic.network.ServiceCreator
import com.AixAic.sss.ui.BottomActivity
import com.AixAic.sss.util.LogUtil
import kotlinx.android.synthetic.main.activity_work_receive.*
import kotlinx.android.synthetic.main.activity_work_submit.description


class WorkReceiveActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(WorkReceiveViewModel::class.java) }
    lateinit var customDialog: CustomDialog

    private lateinit var workReceiveAdapter: WorkReceiveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.rightin_enter,R.anim.rightin_exit)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_receive)
        changeButtonStyle(allReceiveSubmitBtn)
        customDialog = CustomDialog(this, "疯狂打包中")
        if (viewModel.description.isEmpty()){
            viewModel.description = (intent.getStringExtra("description") ?: "") + "\n"+ "提交格式：" +(intent.getStringExtra("fileType") ?: "")
            description.text = viewModel.description
        }
        if (viewModel.stid.isEmpty()){
            viewModel.stid = intent.getStringExtra("stid") ?: "0"
        }
        receive_return.setOnClickListener {
            onBackPressed()
        }

        allReceiveSubmitBtn.setOnClickListener {
            viewModel.status = viewModel.submitAll
            changeButtonStyle(allReceiveSubmitBtn)
            refreshJobList()
        }

        noSubmitReceiveBtn.setOnClickListener {
            viewModel.status = viewModel.noSubmit
            changeButtonStyle(noSubmitReceiveBtn)
            refreshJobList()
        }

        submittedReceiveBtn.setOnClickListener {
            viewModel.status = viewModel.submitted
            changeButtonStyle(submittedReceiveBtn)
            refreshJobList()
        }

        packageFileBtn.setOnClickListener {
            viewModel.packageFile(viewModel.stid.toInt())
            customDialog.show()
        }

        viewModel.receiveResultLiveData.observe(this, Observer { result ->
            val jobResponse = result.getOrNull()
            if (jobResponse != null) {
                viewModel.jobList.clear()
                getJobList(jobResponse.jobList)
                val layoutManager = LinearLayoutManager(this)
                workReceiveRecycler.layoutManager = layoutManager
                workReceiveAdapter = WorkReceiveAdapter(this, viewModel.jobList)
                workReceiveRecycler.adapter = workReceiveAdapter
            } else {
                Toast.makeText(this, "没有作业", Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            receiveRefresh.isRefreshing = false
        })

        viewModel.packageFileLiveData.observe(this, { result ->
            val fileResponse = result.getOrNull()
            if (fileResponse != null) {
                if (fileResponse.status == "ok") {
                    Toast.makeText(this, "打包成功", Toast.LENGTH_LONG).show()
                    val filename = fileResponse.stask.id.toString()+fileResponse.stask.title+".zip"
                    downloadFile(filename)
                    customDialog.dismiss()
                }
            } else {
                Toast.makeText(this, "打包失败", Toast.LENGTH_LONG).show()
            }
        })
        receiveRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshJobList()
        receiveRefresh.setOnRefreshListener {
            refreshJobList()
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.rightout_enter,R.anim.rightout_exit)
    }

    fun getJobList(jobList: List<Job>) {
        when (viewModel.status) {
            viewModel.submitAll -> {
                viewModel.jobList.addAll(jobList)
            }
            viewModel.submitted -> {
                for (job in jobList) {
                    if (job.status == 1) viewModel.jobList.add(job)
                }
            }
            viewModel.noSubmit -> {
                for (job in jobList) {
                    if (job.status == 0) viewModel.jobList.add(job)
                }
            }
        }
    }
    fun refreshJobList() {
        viewModel.refreshJobList(viewModel.stid.toInt())
        receiveRefresh.isRefreshing = true
    }
    fun downloadFile(filename: String) {
        //下载路径，如果路径无效了，可换成你的下载路径
        val url = ServiceCreator.BASE_PACKAGEFILE+filename
        //获取下载管理器
        //创建下载任务,downloadUrl就是下载链接
        val request = DownloadManager.Request(Uri.parse(url))
        //设置什么网络情况下可以下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
        //设置通知栏的标题
        request.setTitle("SSS打包文件");
        //设置通知栏的message
        request.setDescription("${filename}正在下载.....");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //设置漫游状态下是否可以下载
        request.setAllowedOverRoaming(false);

        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf("/")))
        LogUtil.d("下载地址", "${Environment.DIRECTORY_DOWNLOADS}--------${url.substring(url.lastIndexOf("/"))}")
        //获取系统服务
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //进行下载
        val id = downloadManager.enqueue(request)
        val intent = Intent("android.intent.action.MYDOWNLOAD_COMPLETE")
        intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, id)
        intent.putExtra("filename", filename)
        intent.setPackage(packageName)
        sendBroadcast(intent)
    }
    private fun initButtonStyle(){
        allReceiveSubmitBtn.setBackgroundResource(R.drawable.button_normal)
        noSubmitReceiveBtn.setBackgroundResource(R.drawable.button_normal)
        submittedReceiveBtn.setBackgroundResource(R.drawable.button_normal)
        allReceiveSubmitBtn.setTextColor(Color.parseColor("#00D9FF"))
        noSubmitReceiveBtn.setTextColor(Color.parseColor("#00D9FF"))
        submittedReceiveBtn.setTextColor(Color.parseColor("#00D9FF"))
    }
    private fun changeButtonStyle(button: Button) {
        initButtonStyle()
        //change
        button.setBackgroundResource(R.drawable.button_pressed)
        button.setTextColor(Color.parseColor("#FFFFFF"))
    }
}