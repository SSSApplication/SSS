package com.AixAic.sss.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import com.AixAic.sss.R

class WorkPublishActivity : AppCompatActivity() {


    val departmentList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_publish)

        val typeList = ArrayList<String>()
        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)

    }
}