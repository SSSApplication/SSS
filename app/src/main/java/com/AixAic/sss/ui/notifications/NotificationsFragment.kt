package com.AixAic.sss.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.User
import com.AixAic.sss.ui.login.LoginActivity
import com.AixAic.sss.ui.task.WorkPublishActivity
import com.AixAic.sss.util.LogUtil
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(NotificationsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
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

        headPicture.setOnClickListener{
            val intent = Intent(context,WorkPublishActivity::class.java)
            startActivity(intent)
        }

        setting.setOnClickListener {
            val intent = Intent(context,SettingActivity::class.java)
            startActivity(intent)
        }

    }

    fun refreshWeather() {
        viewModel.refreshMyself(viewModel.userName, viewModel.userPassword)
        swipeRefresh.isRefreshing = true
    }

    fun showUserInfo(user: User){
        LogUtil.d("Login", "已登录")
        userName.text = user.name
        userSno.text = user.sno
        school.text = user.school
        organization.text = user.organizationsList[0].organization.name
        if (viewModel.userName.isEmpty()) viewModel.userName = user.sno
        if (viewModel.userPassword.isEmpty()) viewModel.userPassword = user.password
    }
}