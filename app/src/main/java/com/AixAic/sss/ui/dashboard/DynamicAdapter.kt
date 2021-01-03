package com.AixAic.sss.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R

class DynamicAdapter(val dynamicList: List<Dynamic>) : RecyclerView.Adapter<DynamicAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.dUserName)
        val dynamicText: TextView = view.findViewById(R.id.dynamicText)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dynamic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DynamicAdapter.ViewHolder, position: Int) {
        val dynamic = dynamicList[position]
        holder.userName.text = dynamic.userName
        holder.dynamicText.text = dynamic.dynamicText
    }

    override fun getItemCount() = dynamicList.size

}