package com.voterswik.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.voterswik.R
import com.voterswik.adapter.ShareAdapter
import com.voterswik.model.ShareData
import com.voterswik.databinding.ActivityShareBinding
import com.voterswik.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityShareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share)

        binding.ivBack.setOnClickListener(this)

        binding.rvShare.layoutManager = LinearLayoutManager(this)


        val data = ArrayList<ShareData>()

        for (i in 1..10) {
            data.add(ShareData("Test"))
        }

        val adapter = ShareAdapter(this, data)
        binding.rvShare.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                onBackPressed()
            }

        }
    }
}