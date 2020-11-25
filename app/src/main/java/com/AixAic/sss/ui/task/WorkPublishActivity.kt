package com.AixAic.sss.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.AixAic.sss.R

class WorkPublishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_publish)

        val typeList = ArrayList<String>()
        typeList.add("a")
        typeList.add("b")
        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)
        val typeAdapter = ArrayAdapter(this,R.layout.type_spinner,typeList)
        typeSpinner.adapter = typeAdapter


//        val departmentList = ArrayList<String>()
//        departmentList.add("1")
//        departmentList.add("2")
//        val departmentSpinner = findViewById<Spinner>(R.id.departmentSpinner)
//        val deAdapter = ArrayAdapter(this,R.layout.activity_work_publish,departmentList)
//        departmentSpinner.adapter = deAdapter
    }
}