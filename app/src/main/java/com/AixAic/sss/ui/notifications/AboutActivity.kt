package com.AixAic.sss.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.AixAic.sss.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        about_return.setOnClickListener {
            onBackPressed()
        }
    }
}