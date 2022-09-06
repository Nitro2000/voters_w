package com.voterswik.ui.dashboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.voterswik.R
import com.voterswik.adapter.NotificationAdapter
import com.voterswik.databinding.FragmentNotificationBinding
import com.voterswik.ui.BaseFragment
import com.voterswik.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)


        binding.notificationRcy.layoutManager = LinearLayoutManager(requireContext())
        binding.ivDelete.setOnClickListener(this)

        val data = ArrayList<NotificationData>()

        for (i in 1..10) {
            data.add(NotificationData("Test", "Item $i", "21/May/2022"))
        }

        val adapter = NotificationAdapter(requireContext(), data)
        binding.notificationRcy.adapter = adapter

        return binding.root

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivDelete ->{

            }

        }
    }

}