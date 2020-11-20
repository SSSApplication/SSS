package com.AixAic.sss.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.AixAic.sss.R
import com.AixAic.sss.logic.model.Mine

class MineAdapter(private val fragment: NotificationsFragment,val mineList: List<Mine>) : RecyclerView.Adapter<MineAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mineIco: ImageView = view.findViewById(R.id.mine_ico)
        val mineText: TextView = view.findViewById(R.id.mine_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mine_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mine = mineList[position]
        holder.mineIco.setImageResource(mine.mineIco)
        holder.mineText.text = mine.mineText
    }

    override fun getItemCount() = mineList.size
}