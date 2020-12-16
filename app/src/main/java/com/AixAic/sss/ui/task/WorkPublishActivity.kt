package com.AixAic.sss.ui.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.AixAic.sss.R
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.logic.Repository
import com.AixAic.sss.logic.model.Organization
import com.AixAic.sss.ui.BottomActivity
import com.AixAic.sss.util.LogUtil
import kotlinx.android.synthetic.main.activity_work_publish.*
import kotlinx.android.synthetic.main.fragment_home.*

class WorkPublishActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProviders.of(this).get(WorkPublishViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_publish)
        //文件类型下拉框
        val typeList = listOf("图片","Word文档","Excel文档","压缩包")
        val typeSpinner = findViewById<Spinner>(R.id.typeSpinner)
        val typeAdapter = ArrayAdapter(this, R.layout.spinner_item, R.id.spinnertext, typeList)
        typeSpinner.adapter = typeAdapter
        typeSpinner.setOnItemSelectedListener(typeSpinnerListener())

        //发布到：下拉框
        val departmentList = ArrayList<Organization>()
        val user = Repository.getUser()
        for(organizations in user.organizationsList){
            if (organizations.admin == 1) departmentList.add(organizations.organization)
        }
        val departmentSpinner = findViewById<Spinner>(R.id.departmentSpinner)
        val departmentAdapter = ArrayAdapter(this, R.layout.spinner_item, R.id.spinnertext , departmentList)
        departmentSpinner.adapter = departmentAdapter
        departmentSpinner.setOnItemSelectedListener(spinnerListener())

        workSubmitBtn.setOnClickListener {
            viewModel.description = descriptionEdit.text.toString()
            viewModel.title = titleEdit.text.toString()
            viewModel.publishWork(viewModel.id, viewModel.uid, viewModel.oid, viewModel.title, viewModel.description, viewModel.filetype)
            viewModel.resultLiveData.observe(this, Observer { result ->
                val generalResponse = result.getOrNull()
                if (generalResponse != null) {
                    val intent = Intent(SSSApplication.context, BottomActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "发布失败", Toast.LENGTH_LONG).show()
                    result.exceptionOrNull()?.printStackTrace()
                }
            })
        }

        publish_return.setOnClickListener {
            onBackPressed()
        }
    }
    inner class spinnerListener: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selected = parent?.getItemAtPosition(position) as Organization //将选择的转化为Organization的类
            viewModel.oid = selected.id
            LogUtil.d("workPublish", "${selected.id}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            LogUtil.d("workPublish", "什么都没点")
        }

    }

    inner class typeSpinnerListener: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selected = parent?.getItemAtPosition(position).toString()
            viewModel.filetype = selected
            LogUtil.d("workPublish", "${viewModel.filetype}")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            LogUtil.d("workPublish", "什么都没点")
        }

    }

}