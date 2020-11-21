package com.AixAic.sss.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.AixAic.sss.R
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Mine
import com.AixAic.sss.logic.model.User
import com.AixAic.sss.ui.login.LoginActivity
import com.AixAic.sss.util.LogUtil
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {

    private val mineList = ArrayList<Mine>()

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
        val user = Repository.getUser()
        if (viewModel.userName.isEmpty()) viewModel.userName = user.sno
        if (viewModel.userPassword.isEmpty()) viewModel.userPassword = user.password
        viewModel.myselfLiveData.observe(this, {result ->
            val user = result.getOrNull()
            if (user != null){
                showUserInfo(user)
                Repository.saveUser(user)
            }else {
                Toast.makeText(context, "登陆已过期请重新登录！", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false

        })
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        initMine()
        val layoutManager = LinearLayoutManager(activity)
        mineRecycler.layoutManager = layoutManager
        val adapter = MineAdapter(this, mineList)
        mineRecycler.adapter = adapter

    }

    fun refreshWeather() {
        viewModel.refreshMyself(viewModel.userName, viewModel.userPassword)
        swipeRefresh.isRefreshing = true
    }

    fun showUserInfo(user: User){
        LogUtil.d("Login", "已登录")
        userName.setText(user.name)
        userSno.setText(user.sno)
        school.setText(user.school)
        if (viewModel.userName.isEmpty()) viewModel.userName = user.sno
        if (viewModel.userPassword.isEmpty()) viewModel.userPassword = user.password
    }

    private fun initMine() {
        mineList.add(Mine(R.drawable.ic_homework, "我的作业"))
        mineList.add(Mine(R.drawable.ic_mission, "待办事项"))
        mineList.add(Mine(R.drawable.ic_setting, "设置"))
    }
}