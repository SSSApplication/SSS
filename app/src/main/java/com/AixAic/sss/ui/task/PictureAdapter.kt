package com.AixAic.sss.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.model.Sfile
import com.AixAic.sss.logic.network.ServiceCreator
import com.AixAic.sss.util.LogUtil
import com.bumptech.glide.Glide

class PictureAdapter(val activity: WorkSubmitActivity, val pictureList: List<Sfile>) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sfilePicture: ImageView = view.findViewById(R.id.sfilePicture)
        val deletePicture: TextView = view.findViewById(R.id.deletePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_recycler, parent, false)
        val holder = ViewHolder(view)
        holder.deletePicture.setOnClickListener {
            val position = holder.adapterPosition
            val sfile = pictureList[position]
            activity.viewModel.deleteFile(sfile.id)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = pictureList[position]
        val imgUrl = ServiceCreator.BASE_IMG_SMALL+picture.name
        LogUtil.d("imgUrl:", imgUrl)
        Glide.with(SSSApplication.context).load(imgUrl).into(holder.sfilePicture)
    }

    override fun getItemCount() = pictureList.size

}