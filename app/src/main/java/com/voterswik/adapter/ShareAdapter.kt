package com.voterswik.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.R
import com.voterswik.databinding.RowShareListBinding
import com.voterswik.model.ShareData

class ShareAdapter (val context: Context, private var shareData: List<ShareData>)
    : RecyclerView.Adapter<ShareAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(
            R.layout.row_share_list, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = shareData[position]
        holder.binding.userName.text = model.name
          }

    override fun getItemCount(): Int {
        return shareData.size
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding: RowShareListBinding = DataBindingUtil.bind(view)!!
    }
}
