package com.AixAic.sss.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Job
import com.AixAic.sss.ui.task.WorkReceiveActivity

class ReceiveAdapter(private val fragment: HomeFragment,val jobList: List<Job>) : RecyclerView.Adapter<ReceiveAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.jobTitle)
        val jobOwner: TextView = view.findViewById(R.id.jobOwner)
        val checkTask: Button = view.findViewById(R.id.checkTask)
        val toSubmit: Button = view.findViewById(R.id.toSubmit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_recycle_item, parent, false)
        val holder = ViewHolder(view)
        holder.checkTask.setOnClickListener {
            val position = holder.adapterPosition
            val job = jobList[position]
            val intent = Intent(parent.context, WorkReceiveActivity::class.java).apply {
                putExtra("description", job.stask.description)
                putExtra("stid", "${job.stid}")
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.stask.title
        holder.jobOwner.text = "发布人: " + job.user.name
        holder.checkTask.visibility = View.VISIBLE
        holder.toSubmit.visibility = View.GONE
    }

    override fun getItemCount() = jobList.size

}