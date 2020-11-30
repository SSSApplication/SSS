package com.AixAic.sss.ui.home


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
import com.AixAic.sss.SSSApplication
import com.AixAic.sss.ui.task.WorkPublishActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var banner: Banner<BannerDataBean, BannerImageAdapter<BannerDataBean>> = activity!!.findViewById(R.id.banner)
        banner.setAdapter(object : BannerImageAdapter<BannerDataBean>(BannerDataBean.testData) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: BannerDataBean,
                position: Int,
                size: Int
            ) {
                Glide.with(holder.itemView)
                    .load(data.imageUrl)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }).addBannerLifecycleObserver(this).setIndicator(CircleIndicator(SSSApplication.context))

        if (viewModel.user.isAdmin()) {
            receiveBtn.visibility = View.VISIBLE
            receiveBtn.setOnClickListener {
                publishBtn.visibility = View.VISIBLE
                allSubmitBtn.visibility = View.GONE
                noSubmitBtn.visibility = View.GONE
                submittedBtn.visibility = View.GONE
            }
        }

        submitBtn.setOnClickListener {
            publishBtn.visibility = View.GONE
            allSubmitBtn.visibility = View.VISIBLE
            noSubmitBtn.visibility = View.VISIBLE
            submittedBtn.visibility = View.VISIBLE
        }

        publishBtn.setOnClickListener {
            val intent = Intent(context, WorkPublishActivity::class.java)
            startActivity(intent)
        }

    }
}

