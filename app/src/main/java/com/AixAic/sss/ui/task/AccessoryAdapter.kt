package com.AixAic.sss.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Sfile

class AccessoryAdapter(val activity: WorkSubmitActivity, val sfileList: List<Sfile>) : RecyclerView.Adapter<AccessoryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val accessoryName: TextView = view.findViewById(R.id.accessoryName)
        val deleteSfile: TextView = view.findViewById(R.id.deleteSfile)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accessory_recycler, parent, false)
        val holder = ViewHolder(view)
        holder.deleteSfile.setOnClickListener {
            val position = holder.adapterPosition
            val sfile = sfileList[position]
            activity.viewModel.deleteFile(sfile.id)
        }
        return holder
    }

    override fun onBindViewHolder(holder: AccessoryAdapter.ViewHolder, position: Int) {
        val sfile = sfileList[position]
        holder.accessoryName.text = sfile.name
    }

    override fun getItemCount() = sfileList.size

}