package com.AixAic.sss.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Sfile

class AccessoryAdapter(val sfileList: List<Sfile>) : RecyclerView.Adapter<AccessoryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sfileImage: ImageView = view.findViewById(R.id.accessoryName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accessory_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccessoryAdapter.ViewHolder, position: Int) {
        val sfile = sfileList[position]
        holder.sfileImage.setImageResource(sfile.id)
    }

    override fun getItemCount() = sfileList.size

}