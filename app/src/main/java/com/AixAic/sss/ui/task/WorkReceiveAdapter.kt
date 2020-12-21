package com.AixAic.sss.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Job
import com.AixAic.sss.ui.home.HomeFragment

class WorkReceiveAdapter(private val activity: WorkReceiveActivity, val jobList: List<Job>) : RecyclerView.Adapter<WorkReceiveAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val userName: TextView = view.findViewById(R.id.userName)
        val submitStatus: TextView = view.findViewById(R.id.submitStatus)
        val askToSubmit: Button = view.findViewById(R.id.askToSubmit)
        val submitted: Button = view.findViewById(R.id.receiveSubmitted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.work_receive_item, parent, false)
        val holder = ViewHolder(view)
        holder.askToSubmit.setOnClickListener {
            val position = holder.adapterPosition
            val job = jobList[position]
            activity.viewModel.remind(job.id)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobList[position]
        holder.userName.text = job.user.name
        if (job.status == 0) {
            holder.askToSubmit.visibility = View.VISIBLE
            holder.submitted.visibility = View.GONE
            holder.submitStatus.text = "未提交"
        }else {
            holder.askToSubmit.visibility = View.GONE
            holder.submitted.visibility = View.VISIBLE
            holder.submitStatus.text = "已提交"
        }
    }

    override fun getItemCount() = jobList.size
}