package com.AixAic.sss.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.ui.login.LoginActivity


class NotificationsFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(NotificationsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Repository.isUserSaved()){

        }else{
            //如果用户未登录 则返回登录界面
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun refreshWeather() {
        viewModel.refreshMyself(viewModel.userName, viewModel.userPassword)
//        swipeRefresh.isRefreshing = true
    }
}