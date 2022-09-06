package com.voterswik.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.databinding.ActivitySettingBinding
import com.voterswik.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this,R.layout.activity_setting)
        binding.ivBack.setOnClickListener(this)

        binding.layoutInvite.setOnClickListener(this)

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.switchNotification.text = "ON"
            else
                binding.switchNotification.text = "OFF"

        }

        binding.switchAccountPrivate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.switchAccountPrivate.text = "ON"
            else
                binding.switchAccountPrivate.text = "OFF"

        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back ->
            {
               onBackPressed()
            }
            R.id.layout_invite ->
            {
//                startActivity(Intent(this,InviteFriend::class.java))
            }
        }

    }
}