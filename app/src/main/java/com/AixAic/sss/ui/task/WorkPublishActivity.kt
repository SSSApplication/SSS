package com.AixAic.sss.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.AixAic.sss.R
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Organization
import com.AixAic.sss.util.LogUtil

class WorkPublishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_publish)

        val typeList = ArrayList<Organization>()
        val user = Repository.getUser()
        for(organizations in user.organizationsList){
            if (organizations.admin == 1) typeList.add(organizations.organization)
        }
        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)
        val typeAdapter = ArrayAdapter(this, R.layout.spinner_item, R.id.spinnertext , typeList)
        typeSpinner.adapter = typeAdapter
        typeSpinner.setOnItemSelectedListener(spinnerListener())

    }
    class spinnerListener: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selected = parent?.getItemAtPosition(position) as Organization //将选择的转化为Organization的类
            LogUtil.d("workPublish", "${selected.id}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            LogUtil.d("workPublish", "什么都没点")
        }

    }
}