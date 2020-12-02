package com.AixAic.sss.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import kotlinx.android.synthetic.main.activity_work_submit.*

class WorkSubmitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_submit)
        description.text = intent.getStringExtra("description") ?: ""
    }
}