package com.voterswik.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.R
import com.voterswik.databinding.NotificationLayoutBinding
import com.voterswik.model.NotificationData


class NotificationAdapter (val context: Context, private var notificationData: List<NotificationData>)
    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(
            R.layout.notification_layout, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = notificationData[position]
        holder.binding.tvOfferTitle.text = model.title
        holder.binding.tvOfferName.text = model.message
        holder.binding.tvDate.text = model.created_at    }

    override fun getItemCount(): Int {
        return notificationData.size
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding: NotificationLayoutBinding = DataBindingUtil.bind(view)!!
    }
}
