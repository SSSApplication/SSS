package com.AixAic.sss.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Job
import kotlinx.android.synthetic.main.activity_work_publish.view.*

class JobAdapter(private val fragment: HomeFragment,val jobList: List<Job>) : RecyclerView.Adapter<JobAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleEdit)
        val jobOwner: TextView = view.findViewById(R.id.jobOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_recycle_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.stask.title
        holder.title.text = job.user.name
    }

    override fun getItemCount() = jobList.size
}