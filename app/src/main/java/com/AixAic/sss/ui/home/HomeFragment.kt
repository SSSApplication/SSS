package com.AixAic.sss.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Job
import com.AixAic.sss.ui.task.WorkPublishActivity
import com.AixAic.sss.ui.task.WorkSubmitActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.job_recycle_item.*


class HomeFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java)}

    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var banner: Banner<BannerDataBean, BannerImageAdapter<BannerDataBean>> = activity!!.findViewById(R.id.banner)
        banner.setAdapter(object : BannerImageAdapter<BannerDataBean>(BannerDataBean.testData) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: BannerDataBean,
                position: Int,
                size: Int
            ) {
                Glide.with(holder.itemView)
                    .load(data.imageUrl)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }).addBannerLifecycleObserver(this).setIndicator(CircleIndicator(SSSApplication.context))

        if (viewModel.user.isAdmin()) {
            receiveBtn.visibility = View.VISIBLE
            receiveBtn.setOnClickListener {
                publishBtn.visibility = View.VISIBLE
                allSubmitBtn.visibility = View.GONE
                noSubmitBtn.visibility = View.GONE
                submittedBtn.visibility = View.GONE
            }
        }

        submitBtn.setOnClickListener {
            publishBtn.visibility = View.GONE
            allSubmitBtn.visibility = View.VISIBLE
            noSubmitBtn.visibility = View.VISIBLE
            submittedBtn.visibility = View.VISIBLE
            viewModel.status = viewModel.submitAll
            refreshJobList()
        }

        publishBtn.setOnClickListener {
            val intent = Intent(context, WorkPublishActivity::class.java)
            startActivity(intent)
        }

        allSubmitBtn.setOnClickListener {
            viewModel.status = viewModel.submitAll
            refreshJobList()
        }

        noSubmitBtn.setOnClickListener {
            viewModel.status = viewModel.noSubmit
            refreshJobList()
        }

        submittedBtn.setOnClickListener {
            viewModel.status = viewModel.submitted
            refreshJobList()
        }

        receiveBtn.setOnClickListener {
            viewModel.status = viewModel.receive
            refreshJobList()
        }

        viewModel.jobResultLiveData.observe(this, Observer { result ->
            val jobResponse = result.getOrNull()
            if (jobResponse != null) {
                viewModel.jobList.clear()
                getJobList(jobResponse.jobList)
                val layoutManager = LinearLayoutManager(activity)
                jobRecycler.layoutManager = layoutManager
                jobAdapter = JobAdapter(this, viewModel.jobList)
                jobRecycler.adapter = jobAdapter
            } else {
                Toast.makeText(activity, "没有作业", Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            homeRefresh.isRefreshing = false
        })
        homeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshJobList()
        homeRefresh.setOnRefreshListener {
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
            viewModel.receive -> {
                for (job in jobList) {
                    if (job.stask.uid == Repository.getUser().id) viewModel.jobList.add(job)
                }
            }
        }
    }

    fun refreshJobList() {
        viewModel.refreshJobList(viewModel.user.id)
        homeRefresh.isRefreshing = true
    }
}

