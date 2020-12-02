package com.AixAic.sss.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import kotlinx.android.synthetic.main.activity_work_submit.*

class WorkSubmitActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(WorkSubmitViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_submit)
        if (viewModel.description.isEmpty()){
            viewModel.description = intent.getStringExtra("description") ?: ""
            description.text = viewModel.description
        }
        if (viewModel.jid.isEmpty()){
            viewModel.jid = intent.getStringExtra("jid") ?: "0"
        }

    }
}