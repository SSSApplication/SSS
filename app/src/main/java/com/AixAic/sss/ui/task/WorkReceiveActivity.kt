package com.AixAic.sss.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.AixAic.sss.R
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Job
import com.AixAic.sss.util.LogUtil
import kotlinx.android.synthetic.main.activity_work_receive.*
import kotlinx.android.synthetic.main.activity_work_submit.description



class WorkReceiveActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(WorkReceiveViewModel::class.java) }

    private lateinit var workReceiveAdapter: WorkReceiveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_receive)
        if (viewModel.description.isEmpty()){
            viewModel.description = intent.getStringExtra("description") ?: ""
            description.text = viewModel.description
        }
        if (viewModel.stid.isEmpty()){
            viewModel.stid = intent.getStringExtra("stid") ?: "0"
        }

        allReceiveSubmitBtn.setOnClickListener {
            viewModel.status = viewModel.submitAll
            refreshJobList()
        }

        noSubmitReceiveBtn.setOnClickListener {
            viewModel.status = viewModel.noSubmit
            refreshJobList()
        }

        submittedReceiveBtn.setOnClickListener {
            viewModel.status = viewModel.submitted
            refreshJobList()
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
        receiveRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshJobList()
        receiveRefresh.setOnRefreshListener {
            refreshJobList()
        }

    }

    fun getJobList(jobList: List<Job>) {
        when (viewModel.status) {
            viewModel.submitAll -> {
                viewModel.jobList.addAll(jobList)
            }
            viewModel.submitted -> {
                for ( job in jobList){
                    if (job.status == 1) viewModel.jobList.add(job)
                }
            }
            viewModel.noSubmit -> {
                for ( job in jobList){
                    if (job.status == 0) viewModel.jobList.add(job)
                }
            }
        }
    }
    fun refreshJobList() {
        viewModel.refreshJobList(viewModel.stid.toInt())
        receiveRefresh.isRefreshing = true
    }
}