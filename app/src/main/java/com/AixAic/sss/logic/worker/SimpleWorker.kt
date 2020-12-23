package com.AixAic.sss.logic.worker

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.SSSApplication.Companion.context
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.JobResponse
import com.AixAic.sss.logic.model.UserResponse
import com.AixAic.sss.logic.network.JobService
import com.AixAic.sss.logic.network.SSSNetwork
import com.AixAic.sss.logic.network.ServiceCreator
import com.AixAic.sss.logic.network.UserService
import com.AixAic.sss.ui.BottomActivity
import com.AixAic.sss.ui.task.WorkSubmitActivity
import com.AixAic.sss.util.LogUtil
import com.AixAic.sss.util.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
   init {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val channel = NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
           val channel2 = NotificationChannel("important", "Important", NotificationManager.IMPORTANCE_HIGH)
           manager.createNotificationChannel(channel)
       }
   }
    override fun doWork(): Result {
        LogUtil.d("SimpleWorker", "开始获取催交列表")
        val jobService = ServiceCreator.create<JobService>()
        jobService.getRemindList(Repository.getUser().id).enqueue(object : retrofit2.Callback<JobResponse> {
            override fun onResponse(call: Call<JobResponse>, response: Response<JobResponse>) {
                val JobResponse = response.body()
                if (JobResponse != null && JobResponse.status == "ok"){
                    val jobList = JobResponse.jobList
                    if (jobList.isNotEmpty()){
                        for (job in jobList){
                            var intent = Intent(SSSApplication.context, WorkSubmitActivity::class.java).apply {
                                putExtra("description", job.stask.description)
                                putExtra("fileType", job.stask.filetype)
                                putExtra("jid", "${job.id}")
                                putExtra("remind", "1")
                            }
                            LogUtil.d("SimpleWorker", "${job.stask.description} ${job.id}")
                            var pi = PendingIntent.getActivity(SSSApplication.context, job.id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                            val notification = NotificationCompat.Builder(context, "normal")
                                .setContentTitle("提醒您提交 ${job.stask.description} 作业啦")
                                .setSmallIcon(R.drawable.small_icon)
//                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.big_image))
//                        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.resources, R.drawable.big_image)))
                                .setContentIntent(pi)
                                .setAutoCancel(true)
                                .build()
                            manager.notify(job.id, notification)
                        }
                    }
                    LogUtil.d("SimpleWorker", "获取数据成功")
                }else{
                    LogUtil.d("SimpleWorker", "没有数据")
                }
            }
            override fun onFailure(call: Call<JobResponse>, t: Throwable) {
                LogUtil.d("SimpleWorker", "没有数据")
                t.printStackTrace()
            }
        })
        return Result.success()
    }
}

