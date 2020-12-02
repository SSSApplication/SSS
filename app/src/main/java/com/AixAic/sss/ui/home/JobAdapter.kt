package com.AixAic.sss.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Job
import com.AixAic.sss.ui.task.WorkSubmitActivity
import com.AixAic.sss.util.LogUtil

class JobAdapter(private val fragment: HomeFragment,val jobList: List<Job>) : RecyclerView.Adapter<JobAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.jobTitle)
        val jobOwner: TextView = view.findViewById(R.id.jobOwner)
        val toSubmit: Button = view.findViewById(R.id.toSubmit)
        val submitted: Button = view.findViewById(R.id.submitted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_recycle_item, parent, false)
        val holder = ViewHolder(view)
        holder.toSubmit.setOnClickListener {
            val position = holder.adapterPosition
            LogUtil.d("ddd", "${position}")
            val job = jobList[position]
            val intent = Intent(parent.context, WorkSubmitActivity::class.java).apply {
                putExtra("description", job.stask.description)
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.stask.title
        holder.jobOwner.text = "发布人: " + job.user.name
        if (job.status == 0){
            holder.toSubmit.visibility = View.VISIBLE
            holder.submitted.visibility = View.GONE
        }else {
            holder.toSubmit.visibility = View.GONE
            holder.submitted.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = jobList.size
}